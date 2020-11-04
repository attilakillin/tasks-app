package hu.bme.jnsbbk.tasks.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.adapter.TaskListAdapter
import hu.bme.jnsbbk.tasks.persistence.db.AppDatabase
import kotlinx.android.synthetic.main.fragment_task_trash.*
import kotlin.concurrent.thread

class TaskTrashFragment : Fragment(R.layout.fragment_task_trash) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskListAdapter(::showDeletePopup)
        trash_recyclerView.adapter = adapter
        trash_recyclerView.layoutManager = LinearLayoutManager(context)

        AppDatabase.INSTANCE.taskInfoDao().getDeletedTasks().observe(viewLifecycleOwner, {
            adapter.submitList(it)
            if (it.size != 1)
                trash_trashednumberText.text = getString(R.string.n_tasks_in_trash_plural, it.size)
            else
                trash_trashednumberText.text = getString(R.string.n_tasks_in_trash_singular, it.size)
        })

        setEmptyTrashListener()
    }

    private fun setEmptyTrashListener() {
        trash_deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
                .setTitle("Are you sure you want to empty the trash?")
                .setMessage("This action is irreversible!")
                .setPositiveButton("OK") { _, _ ->
                    thread { AppDatabase.INSTANCE.taskDao().hardDeleteMarkedTasks(); }
                }
                .setNegativeButton("Cancel") { _, _ -> Unit }
            builder.create().show()
        }
    }

    private fun showDeletePopup(id: Long) {
        val choices = arrayOf("Restore", "Delete")
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