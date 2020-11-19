package hu.bme.jnsbbk.tasks.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.jnsbbk.tasks.R
import hu.bme.jnsbbk.tasks.Today
import hu.bme.jnsbbk.tasks.adapter.TaskListAdapter
import hu.bme.jnsbbk.tasks.persistence.db.AppDatabase
import kotlinx.android.synthetic.main.dialog_overdue_tasks.view.*
import java.util.*

class OverdueTasksFragment : DialogFragment() {

    private lateinit var dialogView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogView = createView()
        val builder = AlertDialog.Builder(requireContext())
            .setPositiveButton(R.string.ok) { _, _ -> }
            .setView(dialogView)
            .setTitle(getString(R.string.some_tasks_are_overdue))
        return builder.create()
    }

    private fun createView(): View {
        val view: View = requireActivity().layoutInflater.inflate(R.layout.dialog_overdue_tasks, null)
        view.overdue_recyclerview.adapter = TaskListAdapter({})
        view.overdue_recyclerview.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return dialogView // Ez azért kell, mert így meghívódik az onViewCreated
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppDatabase.INSTANCE.taskInfoDao().getOverdueTasks(Today.date).observe(viewLifecycleOwner) {
            (view.overdue_recyclerview.adapter as TaskListAdapter).submitList(it)
        }
    }
}