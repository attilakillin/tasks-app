package hu.bme.jnsbbk.tasks.debug

import android.content.Context
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.persistence.db.AppDatabase
import hu.bme.jnsbbk.tasks.persistence.db.Category
import kotlin.concurrent.thread

object CategoryGenerator {
    fun generateDefaultCategories(context: Context) {
        val light = context.resources.getIntArray(R.array.categoryColorsLight)
        val dark  = context.resources.getIntArray(R.array.categoryColorsDark)

        val values = mapOf(
            "Mikro- és makroökonómia" to 0,
            "Objektumorientált szoftverfejlesztés" to 2,
            "Mesterséges intelligencia" to 3,
            "C11 és C++11 programozás" to 6,
            "IT eszközök technológiája" to 8,
            "Témalaboratórium" to 10,
            "Adatvezérelt rendszerek" to 12,
            "Üzleti jog" to 15,
            "Mobil- és webes szoftverek" to 16
        )

        thread {
            AppDatabase.INSTANCE.categoryDao().deleteEveryCategory()
            for (entry in values) {
                AppDatabase.INSTANCE.categoryDao().insertCategory(
                    Category(
                        cat_id = null,
                        name = entry.key,
                        color_light = String.format("#%06X", 0xFFFFFF and light[entry.value]),
                        color_dark = String.format("#%06X", 0xFFFFFF and dark[entry.value])
                ))
            }
        }
    }
}