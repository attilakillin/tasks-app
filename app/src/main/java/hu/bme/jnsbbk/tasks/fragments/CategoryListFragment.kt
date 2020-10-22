package hu.bme.jnsbbk.tasks.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.adapter.CategoryListAdapter
import hu.bme.jnsbbk.tasks.data.AppDatabase
import hu.bme.jnsbbk.tasks.data.Category
import kotlinx.android.synthetic.main.fragment_category_list.*

class CategoryListFragment : Fragment(R.layout.fragment_category_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CategoryListAdapter(requireActivity(), parentFragmentManager)
        category_recyclerview.adapter = adapter
        category_recyclerview.layoutManager = LinearLayoutManager(context)

        AppDatabase.getInstance(requireContext()).categoryDao().getCategories().observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        categories_add_button.setOnClickListener {
            val cat = Category(
                cat_id = null,
                name = "New Category",
                color_light = "#e4e4e4",
                color_dark = "#e4e4e4"
            )
            val dialog = CategoryEditorDialog(cat, CategoryEditorDialog.Mode.ADD)
            dialog.show(parentFragmentManager, null)
        }
    }
}