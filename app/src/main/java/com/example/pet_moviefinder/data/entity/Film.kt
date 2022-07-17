package com.example.pet_moviefinder.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = Film.Fields.TABLE_NAME)
open class Film (
    @ColumnInfo(name = Fields.ICON_URL)
    @SerializedName(Fields.ICON_URL)open val iconUrl : String?,
    @ColumnInfo(name = Fields.DESCRIPTION)
    @SerializedName(Fields.DESCRIPTION)open val description : String?,
    @PrimaryKey(autoGenerate = false)
    @SerializedName(Fields.ID)open val id : Int,
    @ColumnInfo(name = Fields.TITLE)
    @SerializedName(Fields.TITLE)open val title : String?,
    @ColumnInfo(name = Fields.RATING)
    @SerializedName(Fields.RATING)open val rating : Double?
): Serializable {
    object Fields {
        const val TABLE_NAME = "films_table"
        const val ICON_URL = "poster_path"
        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "overview"
        const val RATING = "vote_average"
    }
}