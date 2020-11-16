package hu.bme.jnsbbk.tasks.fragments.category

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.persistence.db.AppDatabase
import hu.bme.jnsbbk.tasks.persistence.db.Category
import kotlinx.android.synthetic.main.dialog_category_editor.view.*
import kotlin.concurrent.thread

class CategoryEditorDialog(private val category: Category, private val mode: Mode) : DialogFragment() {
    enum class Mode {
        ADD, EDIT
    }

    private lateinit var dialog: AlertDialog
    private var watcherAttached: Boolean = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
            .setPositiveButton(R.string.save) {_, _ -> saveCategory() }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .setView(createView())
        if (mode == Mode.ADD)
            builder.setTitle(R.string.add_new_category)
        else
            builder.setTitle(R.string.edit_category)
        dialog = builder.create()
        return dialog
    }

    override fun onResume() {
        super.onResume()
        if (!watcherAttached) {
            watcherAttached = true
            (dialog.findViewById(R.id.cat_editor_title) as EditText?)!!.doAfterTextChanged {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = it!!.isNotEmpty()
            }
        }
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
        val cp = MaterialColorPickerDialog
            .Builder(requireActivity())
            .setColorShape(ColorShape.SQAURE)

        view.cat_editor_lightcolor.setOnClickListener {
            cp.setColorRes(resources.getIntArray(R.array.categoryColorsLight).toList())
            cp.setColorListener { _, colorHex ->
                category.color_light = colorHex
                view.cat_editor_lightcolor.setBackgroundColor(Color.parseColor(colorHex))
            }
            cp.show()
        }

        view.cat_editor_darkcolor.setOnClickListener {
            cp.setColorRes(resources.getIntArray(R.array.categoryColorsDark).toList())
            cp.setColorListener { _, colorHex ->
                category.color_dark = colorHex
                view.cat_editor_darkcolor.setBackgroundColor(Color.parseColor(colorHex))
            }
            cp.show()
        }
    }

    private fun saveCategory() {
        category.name = (dialog.findViewById(R.id.cat_editor_title) as EditText?)!!.text.toString()
        val dao = AppDatabase.INSTANCE.categoryDao()
        when (mode) {
            Mode.ADD -> thread { dao.insertCategory(category) }
            Mode.EDIT -> thread { dao.updateCategory(category) }
        }
    }
}