package com.parinaz.todolist

import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.parinaz.todolist.adapter.TodoAdapter
import com.parinaz.todolist.databinding.FragmentTodoBinding
import com.parinaz.todolist.domain.Todo
import java.util.*

class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private val args: TodoFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.title = args.todoListName
        binding.name.text = args.todo.name
        binding.checkBox.isChecked = args.todo.done
        val df = DateFormat.format("yyyy/MM/dd", args.todo.createdAt )
        binding.txtDate.text = "created $df"

        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            val newTodo = args.todo.copy(done = isChecked)
            Repository.instance.updateTodo(newTodo)
        }

        binding.deleteBtn.setOnClickListener {
            context?.let {

                AlertDialog.Builder(requireContext())
                    .setTitle("Are you sure?")
                    .setMessage("\"${args.todo.name}\" will be permanently deleted.")
                    .setPositiveButton(
                        "DELETE"
                    ) { _, _ ->
                        Repository.instance.deleteTodo(args.todo)
                        view.findNavController().navigateUp()
                    }
                    .setNegativeButton(
                        "CANCEL"
                    ) { _, _ -> }
                    .show()
            }
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

