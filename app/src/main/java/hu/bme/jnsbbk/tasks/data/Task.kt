package hu.bme.jnsbbk.tasks.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tasks",
        foreignKeys = [ForeignKey(
            entity = Category::class,
            parentColumns = ["cat_id"],
            childColumns = ["category_fk"],
            onDelete = ForeignKey.CASCADE
        )])
data class Task (
    @PrimaryKey(autoGenerate = true) var task_id: Long?,
    @ColumnInfo(name = "category_fk") val category: Long?,
    @ColumnInfo(name = "due_date") val dueDate: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String
)
