package hu.bme.jnsbbk.tasks.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hu.bme.jnsbbk.tasks.fragments.TaskDetailsFragment
import hu.bme.jnsbbk.tasks.fragments.TaskListFragment

class TaskListPagerAdapter(private val fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment = when(position) {
        0 -> TaskListFragment(fm)
        1 -> TaskDetailsFragment(fm)
        else -> TaskListFragment(fm)
    }

    override fun getCount() : Int = 2
}