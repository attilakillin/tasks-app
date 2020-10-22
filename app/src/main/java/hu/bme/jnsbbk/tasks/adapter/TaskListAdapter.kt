package hu.bme.jnsbbk.tasks.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.callback.TaskDiffCallback
import hu.bme.jnsbbk.tasks.data.Task
import kotlinx.android.synthetic.main.item_task_list_row.view.*
/*
class TaskListAdapter(private val data: MutableList<Task>, private val listener: (Task) -> Unit) :
        RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val category = itemView.rowItem_category
        val dueDate  = itemView.rowItem_dueDate
        val title    = itemView.rowItem_title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_task_list_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (category, dueDate, title) = data[holder.adapterPosition]
        holder.category.text = category.name
        holder.dueDate.text = dueDate.toString()
        holder.title.text = title

        val bg: GradientDrawable = holder.itemView.background as GradientDrawable
        bg.color = ColorStateList.valueOf(Color.parseColor(category.color))

        holder.itemView.setOnClickListener {
            listener(data[holder.adapterPosition])
        }
    }

    override fun getItemCount() = data.size

    // TODO Add/Remove item
}
 */

class TaskListAdapter(private val listener: (Long) -> Unit) :
    ListAdapter<Task, TaskListAdapter.ViewHolder>(TaskDiffCallback()) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val category = itemView.taskRow_category
        val dueDate  = itemView.taskRow_dueDate
        val title    = itemView.taskRow_title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_task_list_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = getItem(position)
        // TODO holder.category.text = task.category
        holder.dueDate.text = task.dueDate.toString()
        holder.title.text = task.title

        //val bg: GradientDrawable = holder.itemView.background as GradientDrawable
        //bg.color = ColorStateList.valueOf(Color.parseColor(category.color))

        holder.itemView.setOnClickListener {
            listener(task.task_id!!)
        }
    }
}