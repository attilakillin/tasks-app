package hu.bme.jnsbbk.tasks.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.data.AppDatabase
import hu.bme.jnsbbk.tasks.data.Category
import kotlinx.android.synthetic.main.dialog_category_editor.view.*
import kotlin.concurrent.thread

class CategoryEditorDialog(private val category: Category, private val mode: Mode) : DialogFragment() {
    enum class Mode(val prefix: String) {
        ADD("Add new"),
        EDIT("Edit")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = createView()
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(mode.prefix + " category") // TODO Extract string
            .setPositiveButton("Save") { dialog: DialogInterface, _ ->
                saveCategory(view)
            }
            .setNegativeButton("Cancel") { dialog: DialogInterface, _ ->
                dialog.dismiss()
            }
        builder.setView(view)
        return builder.create()
    }

    private fun createView(): View {
        val view: View = requireActivity().layoutInflater.inflate(R.layout.dialog_category_editor, null)
        view.cat_editor_title.setText(category.name)
        view.cat_editor_lightcolor.setBackgroundColor(Color.parseColor(category.color_light))
        view.cat_editor_darkcolor.setBackgroundColor(Color.parseColor(category.color_dark))

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

        return view
    }

    private fun saveCategory(view: View) {
        // TODO Category name shouldn't be null
        category.name = view.cat_editor_title.text.toString()

        val dao = AppDatabase.getInstance(requireActivity().applicationContext).categoryDao()
        when (mode) {
            Mode.ADD  -> thread { dao.insertCategory(category) }
            Mode.EDIT -> thread { dao.updateCategory(category) }
        }
    }
}