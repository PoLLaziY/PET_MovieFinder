package com.example.pet_moviefinder.model

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.moviefinder.data.remote_api.TheMovieDbConst
import com.example.pet_moviefinder.App
import com.example.pet_moviefinder.R
import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.view.fragments.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface INavigationController {
    fun onNavigationClick(buttonId: Int)
    fun onFilmItemClick(film: Film)
    fun onShareButtonClick(film: Film)
    fun makeSnackBar()
    fun checkPermissions(permission: String): Boolean
    fun requestPermissionAndDo(permission: String)
    suspend fun loadToGalleryImage(film: Film)

    var activity: AppCompatActivity?
}

class NavigationController() : INavigationController {
    var activeFragment: Fragment? = null

    override var activity: AppCompatActivity? = null
        set(value) {
            field = value
            fragmentManager = field?.supportFragmentManager
        }

    var fragmentManager: FragmentManager? = null
        set(value) {
            field = value
            if (value != null) {
                if (activeFragment == null) setFragment(HomeFragment(), R.id.home.toString(), false)
                else value.beginTransaction().add(R.id.fragment_root, activeFragment!!).commit()
            }
        }


    fun setFragment(fragment: Fragment, tag: String? = null, addToBackstack: Boolean = true) {

        if (fragmentManager != null && fragment.javaClass != activeFragment?.javaClass) {
            val tr = fragmentManager!!.beginTransaction().replace(R.id.fragment_root, fragment, tag)
            if (addToBackstack) tr.addToBackStack(null)
            tr.commit()
        }
        activeFragment = fragment
    }


    override fun onNavigationClick(id: Int) {
        when (id) {
            R.id.home -> setFragment(HomeFragment(), R.id.home.toString())
            R.id.favorite -> setFragment(FavoriteFragment(), R.id.favorite.toString())
            R.id.later -> setFragment(LaterFragment(), R.id.later.toString())
            R.id.selections -> setFragment(SelectionsFragment(), R.id.selections.toString())
            R.id.settings -> setFragment(SettingsFragment(), R.id.settings.toString())
        }
    }

    override fun onFilmItemClick(film: Film) {
        if (fragmentManager != null) fragmentManager!!.beginTransaction()
            .add(R.id.fragment_root, DetailsFragment(film)).addToBackStack(null).commit()
    }

    override fun onShareButtonClick(film: Film) {
        if (activity != null) {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "${film.title} ${film.description}")
            intent.type = "text/plain"
            activity!!.startActivity(Intent.createChooser(intent, "Share To:"))
        }
    }

    override fun checkPermissions(permission: String): Boolean {
        if (activity != null) {
            val result = ContextCompat.checkSelfPermission(
                activity!!,
                permission
            )
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    override fun requestPermissionAndDo(permission: String) {
        if (activity != null) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }

    override suspend fun loadToGalleryImage(film: Film) {
        val contentResolver = (activity?: return).contentResolver
        val url = CoroutineScope(Dispatchers.IO).async {
            return@async URL("${TheMovieDbConst.IMAGES_URL}original${film.iconUrl}")
        }
        val bitmap: Bitmap = loadBitmap(url.await())?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, film.title?.handleSingleQuote())
                put(MediaStore.Images.ImageColumns.DATE_ADDED, System.currentTimeMillis())
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MovieFinder")
            }
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            val os = CoroutineScope(Dispatchers.IO).async {
                return@async contentResolver.openOutputStream(uri!!)
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os.await())
            CoroutineScope(Dispatchers.IO).launch {
                os.await()!!.close()
            }
        } else {
            MediaStore.Images.Media.insertImage(contentResolver, bitmap, film.title?.handleSingleQuote(), film.description?.handleSingleQuote())
        }
    }

    private suspend fun loadBitmap(url: URL): Bitmap? {
        return suspendCoroutine {
            val bm = Glide.with(activity!!).asBitmap().load(url).into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    it.resume(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })
        }
    }


    override fun makeSnackBar() {
        if (activeFragment != null && activity != null) {
            Snackbar.make(
                activeFragment!!.requireView(),
                Resources.getSystem().getString(R.string.upload),
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.open) {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.type = Resources.getSystem().getString(R.string.image_type_path)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    activity!!.startActivity(intent)
                }
                .show()
        }
    }



    private fun String.handleSingleQuote(): String {
        return this.replace("'", "")
    }
}