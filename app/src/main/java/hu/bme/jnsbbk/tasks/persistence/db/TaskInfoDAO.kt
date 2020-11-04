package hu.bme.jnsbbk.tasks.persistence.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface TaskInfoDAO {
    @Query("SELECT * FROM taskinfo WHERE is_completed = 0 AND is_in_trash = 0")
    fun getActiveTasks(): LiveData<List<TaskInfo>>

    @Query("SELECT * FROM taskinfo WHERE is_completed = 1 AND is_in_trash = 0")
    fun getCompletedTasks(): LiveData<List<TaskInfo>>

    @Query("SELECT * FROM taskinfo WHERE is_in_trash = 1")
    fun getDeletedTasks(): LiveData<List<TaskInfo>>
}