package hu.bme.jnsbbk.tasks

import java.util.*

/* A lejárt határidejű taszkok lekérdezésénél fontos */
object Today {
    lateinit var date: String private set

    fun initialize() {
        val cal = Calendar.getInstance()
        date = "%d-%02d-%02d".format(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(
            Calendar.DAY_OF_MONTH))
    }
}