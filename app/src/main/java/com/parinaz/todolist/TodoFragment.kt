package com.parinaz.todolist

import android.app.DatePickerDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.parinaz.todolist.databinding.FragmentTodoBinding
import com.parinaz.todolist.domain.Todo
import java.util.*
import kotlin.concurrent.fixedRateTimer

class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private val args: TodoFragmentArgs by navArgs()
    private lateinit var todo: Todo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        todo = Repository.instance.getTodo(args.todoId)
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        registerForContextMenu(binding.dueDateParentLayout)

        activity?.title = todo.name
        binding.name.text = todo.name
        binding.checkBox.isChecked = todo.done
        val df = DateFormat.format("yyyy/MM/dd", todo.createdAt )
        binding.txtDate.text = "created on $df"
        showDueDate()
        binding.txtNote.text = todo.note

        if (todo.important) {
            binding.star.setImageResource(R.drawable.ic_full_star_24)
        }else{
            binding.star.setImageResource(R.drawable.ic_empty_star_24)
        }

        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            todo = todo.copy(done = isChecked)
            Repository.instance.updateTodo(todo)
        }

        binding.deleteBtn.setOnClickListener {
            context?.let {

                AlertDialog.Builder(requireContext())
                    .setTitle("Are you sure?")
                    .setMessage("\"${todo.name}\" will be permanently deleted.")
                    .setPositiveButton(
                        "DELETE"
                    ) { _, _ ->
                        Repository.instance.deleteTodo(todo)
                        view.findNavController().navigateUp()
                    }
                    .setNegativeButton(
                        "CANCEL"
                    ) { _, _ -> }
                    .show()
            }
        }

        binding.view1.setOnClickListener {
            val action =
                TodoFragmentDirections.actionTodoFragmentToNoteFragment(todo)
            view.findNavController().navigate(action)
        }

        binding.star.setOnClickListener {
            val newImportant = !todo.important
            if (newImportant) {
                binding.star.setImageResource(R.drawable.ic_full_star_24)
            }else{
                binding.star.setImageResource(R.drawable.ic_empty_star_24)
            }
            todo = todo.copy(important = newImportant)
            Repository.instance.updateTodo(todo)
        }

        binding.dueDateParentLayout.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.dueDateParentLayout.showContextMenu(binding.txtDueDate.left.toFloat(),binding.txtDueDate.bottom.toFloat())
            } else {
                binding.dueDateParentLayout.showContextMenu()
            }
        }

        binding.dueDateParentLayout.setOnLongClickListener {
            return@setOnLongClickListener true
        }

        binding.imgCancelDueDate.setOnClickListener {
            todo = todo.copy(dueDate = null)
            Repository.instance.updateTodo(todo)
            showDueDate()
            binding.txtDueDate.setTextColor(ContextCompat.getColor(requireContext(),
                R.color.textColor
            ))
            binding.imgCancelDueDate.isVisible = false
        }

        binding.name.setOnClickListener {
            val txtName = EditText(requireContext())
            txtName.setText(todo.name)
            AlertDialog.Builder(requireContext())
                .setTitle("Rename Todo")
                .setView(txtName)
                .setPositiveButton(
                    "SAVE"
                ) { _, _ ->
                    val text = txtName.text.toString()
                    if (text != "") {
                        todo = todo.copy(name = text)
                        Repository.instance.updateTodo(todo)
                        activity?.title = text
                        binding.name.text = todo.name
                    } else {
                        Toast.makeText(context, "Name can not be empty", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                .setNegativeButton(
                    "CANCEL"
                ) { _, _ -> }
                .show()
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        requireActivity().menuInflater.inflate(R.menu.due_date_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        Log.d("ananayis", "onContextItemSelected : $item")
        when(item.itemId) {
            R.id.action_due_pick -> {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                DatePickerDialog(requireContext(), { _, y, m, d ->
                    c.set(y,m,d)
                    todo = todo.copy(dueDate = c.time)
                    Repository.instance.updateTodo(todo)
                    showDueDate()
                }, year, month, day).show()
                return true
            }
            R.id.action_due_today -> {
                todo = todo.copy(dueDate = Date())
                Repository.instance.updateTodo(todo)
                showDueDate()
                return true
            }
            R.id.action_due_tomorrow -> {
                val ms = System.currentTimeMillis() + 24*60*60*1000
                todo = todo.copy(dueDate = Date(ms))
                Repository.instance.updateTodo(todo)
                showDueDate()
                return true
            }
            R.id.action_due_next_week -> {
                val ms = System.currentTimeMillis() + 24*60*60*1000*7
                todo = todo.copy(dueDate = Date(ms))
                Repository.instance.updateTodo(todo)
                showDueDate()
                return true
            }
            else -> {
                return false
            }
        }
    }

    private fun showDueDate() {
        val dueDate = todo.dueDate
        if (dueDate != null) {
            val c = Calendar.getInstance()
            c.time = dueDate
            binding.txtDueDate.text = "Due ${c.get(Calendar.YEAR)}/${c.get(Calendar.MONTH)+1}/${c.get(Calendar.DAY_OF_MONTH)}"
            if (DateUtils.isPastDay(dueDate)){
                binding.txtDueDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                binding.imgCancelDueDate.isVisible = true
            } else {
                binding.txtDueDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                binding.imgCancelDueDate.isVisible = true
            }
        } else {
            binding.txtDueDate.text = "Add due date"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            view?.findNavController()?.navigateUp()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
