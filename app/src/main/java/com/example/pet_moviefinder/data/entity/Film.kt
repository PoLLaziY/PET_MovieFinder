package com.example.pet_moviefinder.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = Film.Fields.TABLE_NAME)
data class Film (
    @ColumnInfo(name = Fields.ICON_URL)
    @SerializedName(Fields.ICON_URL) val iconUrl : String?,
    @ColumnInfo(name = Fields.DESCRIPTION)
    @SerializedName(Fields.DESCRIPTION) val description : String?,
    @PrimaryKey(autoGenerate = false)
    @SerializedName(Fields.ID) val id : Int,
    @ColumnInfo(name = Fields.TITLE)
    @SerializedName(Fields.TITLE) val title : String?,
    @ColumnInfo(name = Fields.RATING)
    @SerializedName(Fields.RATING) val rating : Double?
) {
    object Fields {
        const val TABLE_NAME = "films_table"
        const val ICON_URL = "poster_path"
        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "overview"
        const val RATING = "vote_average"
    }
}