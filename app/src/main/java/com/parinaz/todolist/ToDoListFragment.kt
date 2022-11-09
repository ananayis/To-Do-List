package com.parinaz.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.navArgs
import com.parinaz.todolist.adapter.TodoAdapter
import com.parinaz.todolist.databinding.FragmentTodoListBinding
import android.view.MenuItem
import android.view.*
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.findNavController
import com.parinaz.todolist.domain.Todo
import java.util.*

class ToDoListFragment : Fragment() {

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!
    private val args: ToDoListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.title = args.todoList.name

        val adapter = TodoAdapter(requireContext(), args.todoList.id) {
            val action =
                ToDoListFragmentDirections.actionToDoListFragmentToTodoFragment(it, args.todoList.name)
            view.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter

        binding.fab.setOnClickListener(){
            context?.let {
                val txtName =  EditText(it)
                txtName.hint = "add"

                AlertDialog.Builder(it)
                    .setTitle("add new task")
                    .setMessage("Pleas enter the new task")
                    .setView(txtName)
                    .setPositiveButton("Create"
                    ) { _, _ ->
                        val text = txtName.text.toString()
                        if (text != ""){
                            Repository.instance.addTodo(Todo(text,args.todoList.id, false, Date(),false))
                            binding.recyclerView.adapter = TodoAdapter(it, args.todoList.id) {
                                val action =
                                    ToDoListFragmentDirections.actionToDoListFragmentToTodoFragment(it, args.todoList.name)
                                view.findNavController().navigate(action)
                            }
                        }else{
                            Toast.makeText(context, "Name can not be empty", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Cancel"
                    ) { _, _ -> }
                    .show()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_in_toolbar, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_delete_list -> {
            AlertDialog.Builder(requireContext())
                .setTitle("Are you sure?")
                .setMessage("\"${args.todoList.name}\" will be permanently deleted.")
                .setPositiveButton("DELETE"
                ) { _, _ ->
                    Repository.instance.deleteTodoList(args.todoList.id)
                    view?.findNavController()?.navigateUp()
                }
                .setNegativeButton("CANCEL"
                ) { _, _ -> }
                .show()
            true
        }
        android.R.id.home -> {
            view?.findNavController()?.navigateUp()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
