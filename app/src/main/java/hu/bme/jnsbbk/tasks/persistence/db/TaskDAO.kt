package hu.bme.jnsbbk.tasks.persistence.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDAO {
    @Query("SELECT * FROM tasks")
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE task_id = :id LIMIT 1")
    fun getTask(id: Long): Task?

    @Insert
    fun insertTask(task: Task): Long

    @Update
    fun updateTask(task: Task)

    @Query("UPDATE tasks SET is_in_trash = 1 WHERE task_id = :id")
    fun softDeleteTask(id: Long)

    @Query("UPDATE tasks SET is_in_trash = 0 WHERE task_id = :id")
    fun restoreTask(id: Long)

    @Query("DELETE FROM tasks WHERE task_id = :id")
    fun hardDeleteTask(id: Long)

    @Query("DELETE FROM tasks WHERE is_in_trash = 1")
    fun hardDeleteTrashedTasks()

    @Query("UPDATE tasks SET is_completed = 1 WHERE task_id = :id")
    fun completeTask(id: Long)

    @Query("UPDATE tasks SET is_completed = 0 WHERE task_id = :id")
    fun unCompleteTask(id: Long)

    @Query("UPDATE tasks SET is_in_trash = 1 WHERE is_completed = 1")
    fun softDeleteCompletedTasks()

    @Query("SELECT COUNT(*) FROM tasks WHERE is_completed = 0 AND is_in_trash = 0 AND due_date < :date")
    fun getOverdueTasksAmount(date: String): Long
}