package hu.bme.jnsbbk.tasks.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.callback.CategoryDiffCallback
import hu.bme.jnsbbk.tasks.data.Category
import kotlinx.android.synthetic.main.item_category_list_row.view.*
import hu.bme.jnsbbk.tasks.data.AppDatabase
import hu.bme.jnsbbk.tasks.fragments.CategoryEditorDialog
import kotlin.concurrent.thread

class CategoryListAdapter(private val fm: FragmentManager) :
    ListAdapter<Category, CategoryListAdapter.ViewHolder>(CategoryDiffCallback()) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.catRow_name
        var category: Category? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_category_list_row, parent, false))
        setListeners(holder)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = getItem(position).copy()

        holder.category = category
        holder.name.text = category.name

        val bg: GradientDrawable = holder.itemView.background as GradientDrawable
        bg.color = ColorStateList.valueOf(Color.parseColor(category.color_light))
    }

    private fun setListeners(holder: ViewHolder) {
        holder.itemView.setOnClickListener {
            val dialog = CategoryEditorDialog(holder.category!!, CategoryEditorDialog.Mode.EDIT)
            dialog.show(fm, null)
        }

        holder.itemView.catRow_deleteButton.setOnClickListener {
            val cat = holder.category!!
            thread {
                val dao = AppDatabase.INSTANCE.categoryDao()
                dao.deleteCategory(cat)
            }
        }
    }
}