package hu.bme.jnsbbk.tasks.persistence.db

import androidx.room.DatabaseView

@DatabaseView("""
              SELECT task_id, categories.name AS category, due_date, title,
                     color_light, color_dark, is_completed, is_in_trash
              FROM tasks INNER JOIN categories ON category_fk == cat_id
              ORDER BY due_date""",
    viewName = "taskinfo")
data class TaskInfo (
    val task_id: Long,
    val category: String,
    val due_date: String,
    val title: String,
    val color_light: String,
    val color_dark: String,
    val is_completed: Boolean,
    val is_in_trash: Boolean
)