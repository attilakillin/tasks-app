package hu.bme.jnsbbk.tasks.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CategoryDAO {
    @Query("SELECT * FROM categories")
    fun getCategories(): LiveData<List<Category>>

    @Insert
    fun insertCategory(cat: Category): Long

    @Delete
    fun deleteCategory(cat: Category)

    @Update
    fun updateCategory(cat: Category)
}