package hu.bme.jnsbbk.tasks.fragments

import android.os.Bundle
import android.view.View
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

        val adapter = TaskListAdapter({})
        secondary_recyclerView.adapter = adapter
        secondary_recyclerView.layoutManager = LinearLayoutManager(context)

        AppDatabase.INSTANCE.taskInfoDao().getCompletedTasks().observe(viewLifecycleOwner, {
            adapter.submitList(it)
            if (it.size != 1)
                secondary_ntasksText.text = getString(R.string.n_tasks_completed_plural, it.size)
            else
                secondary_ntasksText.text = getString(R.string.n_tasks_completed_singular, it.size)
        })

        secondary_deleteButton.text = getString(R.string.delete_all)
        secondary_deleteButton.setOnClickListener {
            thread { AppDatabase.INSTANCE.taskDao().softDeleteCompletedTasks() }
        }
    }
}