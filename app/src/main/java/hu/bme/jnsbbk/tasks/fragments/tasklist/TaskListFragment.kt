package hu.bme.jnsbbk.tasks.fragments.tasklist

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.adapter.TaskListAdapter
import hu.bme.jnsbbk.tasks.persistence.db.AppDatabase
import kotlinx.android.synthetic.main.fragment_task_list.*

class TaskListFragment : Fragment(R.layout.fragment_task_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listener: (Long) -> Unit = {
            parentFragmentManager.setFragmentResult("populateDetails", bundleOf("id" to it))
            parentFragmentManager.setFragmentResult("switchToDetails", Bundle.EMPTY)
        }

        val adapter = TaskListAdapter(listener)
        list_recyclerView.adapter = adapter
        list_recyclerView.layoutManager = LinearLayoutManager(context)

        AppDatabase.INSTANCE.taskInfoDao().getActiveTasks().observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        add_task_fab.setOnClickListener {
            parentFragmentManager.setFragmentResult("newEmptyTask", Bundle.EMPTY)
            parentFragmentManager.setFragmentResult("switchToDetails", Bundle.EMPTY)
        }
    }
}