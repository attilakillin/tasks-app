package hu.bme.jnsbbk.tasks.fragments

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.data.*
import kotlinx.android.synthetic.main.fragment_task_details.*
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.thread

class TaskDetailsFragment(private val fm: FragmentManager) : Fragment(R.layout.fragment_task_details) {
    private var task_id: Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (edittext: View in setOf(details_category_edit, details_title_edit, details_description_edit, details_dueDate_edit)) {
            val bg: GradientDrawable = edittext.background as GradientDrawable
            bg.color =
                ColorStateList.valueOf(Color.parseColor(resources.getString(0 + R.color.colorForeground)))
        }
        setDateInputter()

        fm.setFragmentResultListener("populateDetails", viewLifecycleOwner) { _, bundle ->
            val id = bundle.get("id") as Long

            thread {
                val task: Task = AppDatabase.getInstance(requireContext()).taskDao().getTask(id)!!
                view.post { setFields(task) }
            }
        }

        details_doneSave_button.setOnClickListener {
            saveTask()
            clearFields()
            fm.setFragmentResult("switchToList", Bundle())
        }
    }

    private fun setDateInputter() {
        val cal: Calendar = Calendar.getInstance()
        val dateSelected = {datePicker: DatePicker, y: Int, m: Int, d: Int ->
            details_dueDate_edit.setText("%d-%02d-%02d".format(y, m, d))
        }
        val picker = DatePickerDialog(requireContext(), dateSelected,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
        picker.datePicker.minDate = System.currentTimeMillis()

        details_dueDate_edit.setOnClickListener {
            picker.show()
        }
    }

    private fun saveTask() {
        thread {
            val db = AppDatabase.getInstance(requireContext())
            val dao: TaskDAO = db.taskDao()

            val task = Task(
                task_id = task_id,
                category = null, // TODO CATEGORY
                dueDate = details_dueDate_edit.text.toString(),
                title = details_title_edit.text.toString(),
                description = details_description_edit.text.toString()
            )

            db.runInTransaction {
                var exists = false
                if (task_id != null)
                    exists = (dao.getTask(task_id!!) != null)
                if (exists)
                    dao.updateTask(task)
                else
                    dao.insertTask(task)
            }
        }
    }

    private fun setFields(task: Task) {
        task_id = task.task_id
        details_category_edit.setText("")
        details_dueDate_edit.setText(task.dueDate)
        details_title_edit.setText(task.title)
        details_description_edit.setText(task.description)
    }

    private fun clearFields() {
        task_id = null
        details_category_edit.setText("")
        details_dueDate_edit.setText("")
        details_title_edit.setText("")
        details_description_edit.setText("")
    }
}