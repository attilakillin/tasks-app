package hu.bme.jnsbbk.tasks.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface TaskInfoDAO {
    @Query("SELECT * FROM taskinfo")
    fun getTasks(): LiveData<List<TaskInfo>>
}