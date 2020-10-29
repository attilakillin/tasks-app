package hu.bme.jnsbbk.tasks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Category::class, Task::class], views = [TaskInfo::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDAO
    abstract fun categoryDao(): CategoryDAO
    abstract fun taskInfoDao(): TaskInfoDAO

    companion object {
        lateinit var INSTANCE: AppDatabase private set

        fun initialize(context: Context) {
            INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "tasks.db")
                .fallbackToDestructiveMigration().build()
        }
    }
}