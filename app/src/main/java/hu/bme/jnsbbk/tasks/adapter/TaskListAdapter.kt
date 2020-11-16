package hu.bme.jnsbbk.tasks.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.callback.TaskInfoDiffCallback
import hu.bme.jnsbbk.tasks.persistence.db.TaskInfo
import hu.bme.jnsbbk.tasks.persistence.ThemePreferences
import kotlinx.android.synthetic.main.item_task_list_row.view.*

class TaskListAdapter(private val listener: (Long) -> Unit) :
    ListAdapter<TaskInfo, TaskListAdapter.ViewHolder>(TaskInfoDiffCallback()) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val category: TextView = itemView.taskRow_category
        val dueDate: TextView = itemView.taskRow_dueDate
        val title: TextView = itemView.taskRow_title
        val icon: ImageView = itemView.taskRow_icon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_task_list_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = getItem(position).copy()

        holder.category.text = task.category
        holder.dueDate.text = task.due_date
        holder.title.text = task.title

        val color = if (ThemePreferences.darkMode) task.color_dark else task.color_light
        val bg: GradientDrawable = holder.itemView.background as GradientDrawable
        bg.color = ColorStateList.valueOf(Color.parseColor(color))

        if (task.is_completed) {
            holder.icon.setImageResource(R.drawable.ic_completed_24px)
        } else {
            holder.icon.setImageResource(android.R.color.transparent)
        }

        holder.itemView.setOnClickListener { listener(task.task_id) }
    }
}