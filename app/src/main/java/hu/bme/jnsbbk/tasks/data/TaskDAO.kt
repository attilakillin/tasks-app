package hu.bme.jnsbbk.tasks.data

import androidx.lifecycle.LiveData
import androidx.room.*

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

    @Delete
    fun deleteTask(task: Task)
}