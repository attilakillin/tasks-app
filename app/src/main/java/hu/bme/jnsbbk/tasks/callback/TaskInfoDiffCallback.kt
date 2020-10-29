package hu.bme.jnsbbk.tasks.callback

import androidx.recyclerview.widget.DiffUtil
import hu.bme.jnsbbk.tasks.persistence.db.TaskInfo

class TaskInfoDiffCallback : DiffUtil.ItemCallback<TaskInfo>() {
    override fun areItemsTheSame(oldItem: TaskInfo, newItem: TaskInfo): Boolean {
        return oldItem.task_id == newItem.task_id
    }

    override fun areContentsTheSame(oldItem: TaskInfo, newItem: TaskInfo): Boolean {
        return oldItem.category == newItem.category &&
               oldItem.due_date == newItem.due_date &&
               oldItem.title == newItem.title
    }
}