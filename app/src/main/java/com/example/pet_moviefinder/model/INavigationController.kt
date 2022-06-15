package com.example.pet_moviefinder.model

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.pet_moviefinder.R
import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.view.fragments.*

interface INavigationController {
    fun onNavigationClick(buttonId: Int)
    fun onFilmItemClick(film: Film)
    fun onShareButtonClick(film: Film)
    var activity: AppCompatActivity?
}

class NavigationController(): INavigationController {
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
        if (fragmentManager != null) fragmentManager!!.beginTransaction().add(R.id.fragment_root, DetailsFragment(film)).addToBackStack(null).commit()
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
}