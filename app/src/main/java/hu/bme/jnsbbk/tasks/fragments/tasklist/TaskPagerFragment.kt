package hu.bme.jnsbbk.tasks.fragments.tasklist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.adapter.TaskListPagerAdapter
import kotlinx.android.synthetic.main.fragment_task_pager.*

class TaskPagerFragment : Fragment(R.layout.fragment_task_pager) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewpager_tasklist.adapter = TaskListPagerAdapter(childFragmentManager)

        childFragmentManager.setFragmentResultListener("switchToList", viewLifecycleOwner) { _, _ ->
            viewpager_tasklist.currentItem = 0
        }
        childFragmentManager.setFragmentResultListener("switchToDetails", viewLifecycleOwner) { _, _ ->
            viewpager_tasklist.currentItem = 1
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbarmenu_list, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    /* Igaz, ha feldolgozta a kérést, hamis, ha nem, és a hívónak kell */
    fun processBackButtonPress(): Boolean {
        if (viewpager_tasklist.currentItem != 0) {
            childFragmentManager.setFragmentResult("backPressed", Bundle.EMPTY)
            viewpager_tasklist.currentItem = 0
            return true
        }
        return false
    }
}