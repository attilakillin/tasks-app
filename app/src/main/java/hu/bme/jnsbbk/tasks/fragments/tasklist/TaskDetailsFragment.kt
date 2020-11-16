package hu.bme.jnsbbk.tasks.fragments.tasklist

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.adapter.CategorySpinnerAdapter
import hu.bme.jnsbbk.tasks.persistence.db.AppDatabase
import hu.bme.jnsbbk.tasks.persistence.db.Category
import hu.bme.jnsbbk.tasks.persistence.db.Task
import hu.bme.jnsbbk.tasks.persistence.db.TaskDAO
import kotlinx.android.synthetic.main.fragment_task_details.*
import java.util.*
import kotlin.concurrent.thread

class TaskDetailsFragment : Fragment(R.layout.fragment_task_details) {
    private companion object {
        var task_id: Long? = null
        var current_mode: Mode = Mode.EDIT
    }
    private lateinit var categories: List<Category>

    private enum class Mode {
        VIEW, EDIT
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEditTextColors()
        setUpCategorySelector()
        setUpDateEditor()
        setMode(current_mode)
        addEditorButtonListeners()

        parentFragmentManager.setFragmentResultListener("populateDetails", viewLifecycleOwner) { _, bundle ->
            val id = bundle.get("id") as Long
            thread {
                val task: Task = AppDatabase.INSTANCE.taskDao().getTask(id)!!
                view.post {
                    setMode(Mode.VIEW)
                    setFields(task)
                }
            }
        }

        parentFragmentManager.setFragmentResultListener("backPressed", viewLifecycleOwner) { _, _ ->
            if (current_mode == Mode.VIEW) {
                clearFields()
                setMode(Mode.EDIT)
            }
        }

        parentFragmentManager.setFragmentResultListener("newEmptyTask", viewLifecycleOwner) { _, _ ->
            clearFields()
            setMode(Mode.EDIT)
        }
    }

    private fun addEditorButtonListeners() {
        details_delete_button.setOnClickListener {
            val idCopy = task_id!!
            thread {
                AppDatabase.INSTANCE.taskDao().softDeleteTask(idCopy)
            }
            clearFields()
            setMode(Mode.EDIT)
            parentFragmentManager.setFragmentResult("switchToList", Bundle.EMPTY)
        }
        details_edit_button.setOnClickListener {
            setMode(Mode.EDIT)
        }
    }

    private fun setMode(mode: Mode) {
        current_mode = mode
        refreshDoneButtonColor()
        when (mode) {
            Mode.VIEW -> {
                enableViewsForEditing(false)
                details_doneSave_button.setOnClickListener {
                    val id = task_id
                    if (id != null) thread { AppDatabase.INSTANCE.taskDao().completeTask(id) }
                    Toast.makeText(requireContext(), getString(R.string.task_completed_toast), Toast.LENGTH_SHORT).show()
                    clearFields()
                    setMode(Mode.EDIT)
                    parentFragmentManager.setFragmentResult("switchToList", Bundle.EMPTY)
                }
            }
            Mode.EDIT -> {
                enableViewsForEditing(true)
                details_doneSave_button.setOnClickListener {
                    if (!saveTask()) return@setOnClickListener
                    clearFields()
                    parentFragmentManager.setFragmentResult("switchToList", Bundle.EMPTY)
                }
            }
        }
    }

    private fun refreshDoneButtonColor() {
        when (current_mode) {
            Mode.VIEW -> {
                details_doneSave_button.text = resources.getString(R.string.done)
                details_doneSave_button.backgroundTintList = AppCompatResources
                    .getColorStateList(requireContext(), R.color.colorGreen)
                details_doneSave_button.setTextColor(AppCompatResources
                    .getColorStateList(requireContext(), R.color.colorTextDark))
            }
            Mode.EDIT -> {
                details_doneSave_button.text = resources.getString(R.string.save)
                details_doneSave_button.backgroundTintList =
                    AppCompatResources.getColorStateList(requireContext(), R.color.colorAccent)
                details_doneSave_button.setTextColor(AppCompatResources
                    .getColorStateList(requireContext(), R.color.colorTextLight))
            }
        }
    }

    private fun enableViewsForEditing(value: Boolean) {
        details_category_edit.isEnabled = value
        details_dueDate_edit.isEnabled = value
        details_title_edit.isEnabled = value
        details_description_edit.isEnabled = value

        details_delete_button.isEnabled = !value
        details_delete_button.isVisible = !value

        details_edit_button.isEnabled = !value
        details_edit_button.isVisible = !value
    }

    private fun setUpCategorySelector() {
        AppDatabase.INSTANCE.categoryDao().getCategories().observe(viewLifecycleOwner, {
            categories = it
            details_category_edit.adapter = CategorySpinnerAdapter(requireContext(), it)
        })
    }

    private fun setEditTextColors() {
        for (view: View in setOf(
            details_category_edit,
            details_title_edit,
            details_description_edit,
            details_dueDate_edit
        )) {
            (view.background as GradientDrawable).color =
                ColorStateList.valueOf(Color.parseColor(resources.getString(0 + R.color.colorForeground)))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpDateEditor() {
        val cal: Calendar = Calendar.getInstance()
        val dateSelected = { _: DatePicker, y: Int, m: Int, d: Int ->
            details_dueDate_edit.setText("%d-%02d-%02d".format(y, m + 1, d))
        }
        val picker = DatePickerDialog(requireContext(), dateSelected,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
        picker.datePicker.minDate = System.currentTimeMillis()

        details_dueDate_edit.setOnClickListener { picker.show() }
    }

    private fun saveTask(): Boolean {
        val task = Task(
            task_id = task_id,
            category = (details_category_edit.adapter as CategorySpinnerAdapter)
                .getCategoryId(details_category_edit.selectedItemPosition),
            dueDate = details_dueDate_edit.text.toString(),
            title = details_title_edit.text.toString(),
            description = details_description_edit.text.toString()
        )

        if (task.title == "") {
            Toast.makeText(requireContext(),getString(R.string.please_enter_title_toast),
                Toast.LENGTH_SHORT).show()
            return false
        }
        if (task.dueDate == "") {
            Toast.makeText(requireContext(), getString(R.string.please_choose_due_date_toast),
                Toast.LENGTH_SHORT).show()
            return false
        }

        thread {
            val db = AppDatabase.INSTANCE
            val dao: TaskDAO = db.taskDao()

            db.runInTransaction {
                var exists = false
                if (task.task_id != null)
                    exists = (dao.getTask(task.task_id) != null)
                if (exists)
                    dao.updateTask(task)
                else
                    dao.insertTask(task)
            }
        }

        return true
    }

    private fun setFields(task: Task) {
        task_id = task.task_id
        details_category_edit.setSelection(categories.indexOfFirst {
            it.cat_id!! == task.category
        })
        details_dueDate_edit.setText(task.dueDate)
        details_title_edit.setText(task.title)
        details_description_edit.setText(task.description)
    }

    private fun clearFields() {
        task_id = null
        details_category_edit.setSelection(0)
        details_dueDate_edit.setText("")
        details_title_edit.setText("")
        details_description_edit.setText("")
    }
}