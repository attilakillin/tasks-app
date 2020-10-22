package hu.bme.jnsbbk.tasks.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.adapter.TaskListPagerAdapter
import kotlinx.android.synthetic.main.fragment_task_pager.*

class TaskPagerFragment : Fragment(R.layout.fragment_task_pager) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewpager_tasklist.adapter = TaskListPagerAdapter(childFragmentManager)

        childFragmentManager.setFragmentResultListener("switchToList", viewLifecycleOwner) { key, bundle ->
            viewpager_tasklist.currentItem = 0
        }
        childFragmentManager.setFragmentResultListener("switchToDetails", viewLifecycleOwner) { key, bundle ->
            viewpager_tasklist.currentItem = 1
        }
    }
}