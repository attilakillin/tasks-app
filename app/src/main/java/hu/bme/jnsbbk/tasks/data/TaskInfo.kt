package hu.bme.jnsbbk.tasks.data

import androidx.room.DatabaseView

@DatabaseView("""SELECT task_id, categories.name AS category, due_date, title, color_light, color_dark
              FROM tasks INNER JOIN categories ON category_fk == cat_id""")
data class TaskInfo (
    val task_id: Long,
    val category: String,
    val dueDate: String,
    val title: String,
    val color_light: String,
    val color_dark: String
)
