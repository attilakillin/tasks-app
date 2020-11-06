package hu.bme.jnsbbk.tasks.fragments.category

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.persistence.db.AppDatabase
import hu.bme.jnsbbk.tasks.persistence.db.Category
import kotlinx.android.synthetic.main.dialog_category_editor.view.*
import kotlin.concurrent.thread

class CategoryEditorDialog(private val category: Category, private val mode: Mode) : DialogFragment() {
    enum class Mode {
        ADD, EDIT
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = createView()
        val builder = AlertDialog.Builder(requireContext())
            .setPositiveButton(R.string.save) {_, _ -> saveCategory(view) }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
        if (mode == Mode.ADD)
            builder.setTitle(R.string.add_new_category)
        else
            builder.setTitle(R.string.edit_category)
        builder.setView(view)
        return builder.create()
    }

    private fun createView(): View {
        val view: View = requireActivity().layoutInflater.inflate(R.layout.dialog_category_editor, null)
        view.cat_editor_title.setText(category.name)
        view.cat_editor_lightcolor.setBackgroundColor(Color.parseColor(category.color_light))
        view.cat_editor_darkcolor.setBackgroundColor(Color.parseColor(category.color_dark))

        val bg: GradientDrawable = view.cat_editor_title.background as GradientDrawable
        bg.color = ColorStateList.valueOf(Color.parseColor(resources.getString(0+R.color.colorForeground)))

        initializeColorPickers(view)

        return view
    }

    private fun initializeColorPickers(view: View) {
        val cp = ColorPicker(activity)
        cp.enableAutoClose()

        view.cat_editor_lightcolor.setOnClickListener {
            cp.color = Color.parseColor(category.color_light)
            cp.setCallback { color ->
                val value = String.format("#%06X", (0xFFFFFF and color))
                category.color_light = value
                view.cat_editor_lightcolor.setBackgroundColor(Color.parseColor(value))
            }
            cp.show()
        }

        view.cat_editor_darkcolor.setOnClickListener {
            cp.color = Color.parseColor(category.color_dark)
            cp.setCallback { color ->
                val value = String.format("#%06X", (0xFFFFFF and color))
                category.color_dark = value
                view.cat_editor_darkcolor.setBackgroundColor(Color.parseColor(value))
            }
            cp.show()
        }
    }

    private fun saveCategory(view: View) {
        category.name = view.cat_editor_title.text.toString()
        if (category.name == "") return // TODO Check if empty

        val dao = AppDatabase.INSTANCE.categoryDao()
        when (mode) {
            Mode.ADD -> thread { dao.insertCategory(category) }
            Mode.EDIT -> thread { dao.updateCategory(category) }
        }
    }
}