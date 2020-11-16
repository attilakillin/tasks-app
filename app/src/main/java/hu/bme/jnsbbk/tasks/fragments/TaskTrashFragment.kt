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

class TaskTrashFragment : Fragment(R.layout.fragment_task_secondary) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskListAdapter(::showDeletePopup)
        secondary_recyclerView.adapter = adapter
        secondary_recyclerView.layoutManager = LinearLayoutManager(context)

        AppDatabase.INSTANCE.taskInfoDao().getDeletedTasks().observe(viewLifecycleOwner, {
            adapter.submitList(it)
            if (it.size != 1)
                secondary_ntasksText.text = getString(R.string.n_tasks_in_trash_plural, it.size)
            else
                secondary_ntasksText.text = getString(R.string.n_tasks_in_trash_singular, it.size)
        })

        secondary_deleteButton.text = getString(R.string.empty_trash)
        setEmptyTrashListener()
    }

    private fun setEmptyTrashListener() {
        secondary_deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.empty_trash_alert_title))
                .setMessage(getString(R.string.empty_trash_alert_message))
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    thread { AppDatabase.INSTANCE.taskDao().hardDeleteTrashedTasks() }
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ -> Unit }
            builder.create().show()
        }
    }

    private fun showDeletePopup(id: Long) {
        val choices = arrayOf(getString(R.string.restore), getString(R.string.delete))
        val builder = AlertDialog.Builder(requireContext())
            .setItems(choices) {_, which ->
                when (which) {
                    0 -> { thread { AppDatabase.INSTANCE.taskDao().restoreTask(id) }}
                    1 -> { thread { AppDatabase.INSTANCE.taskDao().hardDeleteTask(id) }}
                }
            }
        builder.create().show()
    }
}