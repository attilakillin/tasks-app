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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCategory(cat: Category): Long

    @Update
    fun updateCategory(cat: Category)

    @Delete
    fun deleteCategory(cat: Category)

    @Transaction
    fun checkAndInsertNoCategory() {
        val nocat = Category(
            cat_id = 0,
            name = "No category",
            color_light = "#E0E0E0",
            color_dark = "#292929"
        )
        insertCategory(nocat)
    }

    @Query("DELETE FROM categories WHERE cat_id != 0")
    fun deleteEveryCategory()
}