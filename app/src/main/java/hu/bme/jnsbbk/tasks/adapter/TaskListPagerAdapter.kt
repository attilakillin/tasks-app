package hu.bme.jnsbbk.tasks.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hu.bme.jnsbbk.tasks.fragments.TaskDetailsFragment
import hu.bme.jnsbbk.tasks.fragments.TaskListFragment
/*
class TaskListPagerAdapter(parent: Fragment) :
        FragmentStateAdapter(parent) {
    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> TaskListFragment()
        1 -> TaskDetailsFragment()
        else -> TaskListFragment()
    }

    override fun getItemCount() : Int = pages

    var pages = 2
}

*/
class TaskListPagerAdapter(val fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment = when(position){
        0 -> TaskListFragment()
        1 -> TaskDetailsFragment()
        else -> TaskListFragment()
    }

    override fun getCount() : Int = pages

    var pages = 2
}
