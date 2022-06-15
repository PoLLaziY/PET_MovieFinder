package com.example.pet_moviefinder.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@Entity(tableName = FavoriteFilm.Fields.TABLE_NAME)
data class FavoriteFilm (
    @ColumnInfo(name = Film.Fields.ICON_URL)
    @SerializedName(Film.Fields.ICON_URL) val iconUrl : String?,
    @ColumnInfo(name = Film.Fields.DESCRIPTION)
    @SerializedName(Film.Fields.DESCRIPTION) val description : String?,
    @PrimaryKey(autoGenerate = false)
    @SerializedName(Film.Fields.ID) val id : Int,
    @ColumnInfo(name = Film.Fields.TITLE)
    @SerializedName(Film.Fields.TITLE) val title : String?,
    @ColumnInfo(name = Film.Fields.RATING)
    @SerializedName(Film.Fields.RATING) val rating : Double?
) {
    object Fields {
        const val TABLE_NAME = "favorite_film_table"
    }
}