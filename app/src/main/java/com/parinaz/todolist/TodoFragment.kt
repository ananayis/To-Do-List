package com.parinaz.todolist

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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
        todo = args.todo
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("color", "" + binding.txtDueDate.currentTextColor)
        activity?.title = args.todoListName
        binding.name.text = todo.name
        binding.checkBox.isChecked = todo.done
        val df = DateFormat.format("yyyy/MM/dd", todo.createdAt )
        binding.txtDate.text = "created $df"
        showDueDate()

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
