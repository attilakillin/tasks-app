package hu.bme.jnsbbk.tasks.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import hu.bme.jnsbbk.tasks.persistence.db.Category

class CategorySpinnerAdapter(context: Context, private val cats: List<Category>)
    : ArrayAdapter<Category>(context, android.R.layout.simple_spinner_item, cats) {

    fun getCategoryId(position: Int): Long {
        return cats[position].cat_id!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.text = cats[position].name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.text = cats[position].name

        val dpToPxScale = context.resources.displayMetrics.density
        val pxPadding = (10 * dpToPxScale + 0.5f).toInt()
        view.setPadding(pxPadding, pxPadding, pxPadding, pxPadding)
        return view
    }
}