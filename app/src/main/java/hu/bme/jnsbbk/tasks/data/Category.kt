package hu.bme.jnsbbk.tasks.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) var cat_id: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "color_light") var color_light: String,
    @ColumnInfo(name = "color_dark") var color_dark: String
)