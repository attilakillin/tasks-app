package hu.bme.jnsbbk.tasks.persistence.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CategoryDAO {
    @Query("SELECT * FROM categories")
    fun getCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM categories WHERE cat_id = :id LIMIT 1")
    fun getCategory(id: Long): Category?

    @Query("SELECT cat_id FROM categories")
    fun getCategoryIDs(): List<Long?>

    @Insert
    fun insertCategory(cat: Category): Long

    @Update
    fun updateCategory(cat: Category)

    @Delete
    fun deleteCategory(cat: Category)
}