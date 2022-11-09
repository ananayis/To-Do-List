package com.parinaz.todolist

import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.parinaz.todolist.databinding.FragmentTodoBinding
import com.parinaz.todolist.domain.Todo

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

        activity?.title = args.todoListName
        binding.name.text = todo.name
        binding.checkBox.isChecked = todo.done
        val df = DateFormat.format("yyyy/MM/dd", todo.createdAt )
        binding.txtDate.text = "created $df"

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
