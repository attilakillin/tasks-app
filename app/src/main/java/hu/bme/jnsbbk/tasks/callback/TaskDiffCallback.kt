package hu.bme.jnsbbk.tasks.callback

import androidx.recyclerview.widget.DiffUtil
import hu.bme.jnsbbk.tasks.data.Task

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.task_id == newItem.task_id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}