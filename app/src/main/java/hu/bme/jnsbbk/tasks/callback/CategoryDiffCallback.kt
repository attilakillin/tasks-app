package hu.bme.jnsbbk.tasks.callback

import androidx.recyclerview.widget.DiffUtil
import hu.bme.jnsbbk.tasks.data.Category

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.cat_id == newItem.cat_id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return  oldItem.name == newItem.name &&
                oldItem.color_light == newItem.color_light &&
                oldItem.color_dark == newItem.color_dark
    }
}