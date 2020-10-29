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

    @Query("DELETE FROM tasks WHERE task_id = :id")
    fun deleteTaskById(id: Long)
}