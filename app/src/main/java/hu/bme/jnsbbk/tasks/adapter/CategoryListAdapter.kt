package hu.bme.jnsbbk.tasks.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.callback.CategoryDiffCallback
import hu.bme.jnsbbk.tasks.persistence.db.Category
import kotlinx.android.synthetic.main.item_category_list_row.view.*
import hu.bme.jnsbbk.tasks.persistence.db.AppDatabase
import hu.bme.jnsbbk.tasks.persistence.ThemePreferences
import hu.bme.jnsbbk.tasks.fragments.category.CategoryEditorDialog
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

        val color = if (ThemePreferences.darkMode) category.color_dark else category.color_light
        val bg: GradientDrawable = holder.itemView.background as GradientDrawable
        bg.color = ColorStateList.valueOf(Color.parseColor(color))

        holder.itemView.catRow_deleteButton.isVisible = (category.cat_id!! != 0L)
    }

    private fun setListeners(holder: ViewHolder) {
        holder.itemView.setOnClickListener {
            if (holder.category!!.cat_id!! == 0L) return@setOnClickListener // No category behavior
            val dialog = CategoryEditorDialog(holder.category!!, CategoryEditorDialog.Mode.EDIT)
            dialog.show(fm, null)
        }

        holder.itemView.catRow_deleteButton.setOnClickListener {
            val cat = holder.category!!
            confirmThenDo(holder.itemView.context) {
                thread {
                    val dao = AppDatabase.INSTANCE.categoryDao()
                    dao.deleteCategory(cat)
                }
            }
        }
    }

    private fun confirmThenDo(context: Context, deleteAction: Runnable) {
        val builder = AlertDialog.Builder(context)
            .setTitle("Are you sure?")
            .setMessage("Deleting this category will delete every task associated with it!")
            .setPositiveButton("OK") { _, _ -> deleteAction.run() }
            .setNegativeButton("Cancel") { _, _ -> Unit }
        builder.create().show()
    }
}