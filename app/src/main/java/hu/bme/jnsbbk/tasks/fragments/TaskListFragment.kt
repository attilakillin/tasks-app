package hu.bme.jnsbbk.tasks.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.adapter.TaskListAdapter
import hu.bme.jnsbbk.tasks.data.AppDatabase
import hu.bme.jnsbbk.tasks.data.Category
import hu.bme.jnsbbk.tasks.data.Task
import kotlinx.android.synthetic.main.fragment_task_list.*
import java.time.LocalDate
import java.time.Month

class TaskListFragment() : Fragment(R.layout.fragment_task_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fm = this.requireActivity().supportFragmentManager
        val listener: (Long) -> Unit = {
            fm.setFragmentResult("populateDetails", bundleOf("id" to it))
            fm.setFragmentResult("switchToDetails", Bundle())
        }

        val adapter = TaskListAdapter(listener)
        list_recyclerView.adapter = adapter
        list_recyclerView.layoutManager = LinearLayoutManager(context)

        AppDatabase.getInstance(requireContext()).taskDao().getTasks().observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }
}