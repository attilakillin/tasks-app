package hu.bme.jnsbbk.tasks.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.adapter.TaskListAdapter
import hu.bme.jnsbbk.tasks.persistence.db.AppDatabase
import kotlinx.android.synthetic.main.fragment_task_secondary.*
import kotlin.concurrent.thread

class TaskCompletedFragment : Fragment(R.layout.fragment_task_secondary) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskListAdapter(::showRestorePopup)
        secondary_recyclerView.adapter = adapter
        secondary_recyclerView.layoutManager = LinearLayoutManager(context)

        AppDatabase.INSTANCE.taskInfoDao().getCompletedTasks().observe(viewLifecycleOwner, {
            adapter.submitList(it)
            secondary_ntasksText.text = resources.getQuantityString(R.plurals.n_tasks_completed, it.size, it.size)
        })

        secondary_deleteButton.text = getString(R.string.delete_all)
        secondary_deleteButton.setOnClickListener {
            thread { AppDatabase.INSTANCE.taskDao().softDeleteCompletedTasks() }
        }
    }

    private fun showRestorePopup(id: Long) {
        val choices = arrayOf(getString(R.string.mark_not_completed))
        val builder = AlertDialog.Builder(requireContext())
            .setItems(choices) {_, which ->
                when (which) {
                    0 -> { thread { AppDatabase.INSTANCE.taskDao().unCompleteTask(id) }}
                }
            }
        builder.create().show()
    }
}