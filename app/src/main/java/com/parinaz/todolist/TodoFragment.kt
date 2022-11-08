package com.parinaz.todolist

import android.os.Bundle
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

        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            val newTodo = args.todo.copy(done = isChecked)
            Repository.instance.updateTodo(newTodo)
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
