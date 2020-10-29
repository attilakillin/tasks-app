package hu.bme.jnsbbk.tasks.debug

import hu.bme.jnsbbk.tasks.persistence.db.AppDatabase
import hu.bme.jnsbbk.tasks.persistence.db.Task
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object TaskGenerator {
    fun generateTask() {
        val cal: Calendar = Calendar.getInstance()
        val gcal = GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
        val y = ThreadLocalRandom.current().nextInt(cal.get(Calendar.YEAR) + 1, cal.get(Calendar.YEAR) + 3)
        val m = ThreadLocalRandom.current().nextInt(gcal.getActualMinimum(Calendar.MONTH),
                                                    gcal.getActualMaximum(Calendar.MONTH))
        val d = ThreadLocalRandom.current().nextInt(gcal.getActualMinimum(Calendar.DAY_OF_MONTH),
                                                    gcal.getActualMaximum(Calendar.DAY_OF_MONTH))

        val builder = StringBuilder(10)
        for (i in 0..10) {
            builder.append(ThreadLocalRandom.current().nextInt('a'.toInt(), 'z'.toInt() + 1).toChar())
        }


        val db = AppDatabase.INSTANCE
        db.runInTransaction {
            val cats = db.categoryDao().getCategoryIDs()
            val cat_idx = ThreadLocalRandom.current().nextInt(0, cats.size)
            val task = Task(
                task_id = null,
                category = cats.get(cat_idx)!!,
                dueDate = "%d-%02d-%02d".format(y, m, d),
                title = builder.toString(),
                description = "Placeholder description"
            )
            db.taskDao().insertTask(task)
        }
    }
}