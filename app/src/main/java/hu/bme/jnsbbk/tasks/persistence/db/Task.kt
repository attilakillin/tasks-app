package hu.bme.jnsbbk.tasks.persistence.db

import androidx.room.*

@Entity(tableName = "tasks",
        foreignKeys = [ForeignKey(
            entity = Category::class,
            parentColumns = ["cat_id"],
            childColumns = ["category_fk"],
            onDelete = ForeignKey.CASCADE
        )],
        indices = [
            Index(value = ["category_fk"]),
            Index(value = ["due_date"])
        ])
data class Task (
    @PrimaryKey(autoGenerate = true) var task_id: Long?,
    @ColumnInfo(name = "category_fk") val category: Long?,
    @ColumnInfo(name = "due_date") val dueDate: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "is_completed") val completed: Boolean = false,
    @ColumnInfo(name = "is_in_trash") val inTrash: Boolean = false
)