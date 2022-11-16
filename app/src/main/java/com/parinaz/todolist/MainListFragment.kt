package com.parinaz.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parinaz.todolist.databinding.FragmentMainListBinding
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.parinaz.todolist.adapter.TodoListAdapter

class MainListFragment : Fragment() {

    private var _binding: FragmentMainListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.title = getString(R.string.app_name)

        val toDoLists = Repository.instance.getTodoLists()
        val adapter = TodoListAdapter(requireContext(), toDoLists) {
            val action =
                MainListFragmentDirections
                    .actionMainListFragmentToToDoListFragment(todoList = it)
            view.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter

        binding.layoutNewList.setOnClickListener(){
            context?.let {
                val txtName =  EditText(it)
                txtName.hint = "Enter list title"

                AlertDialog.Builder(it)
                    .setTitle("New list")
                    .setMessage("Pleas enter the new list name")
                    .setView(txtName)
                    .setPositiveButton("Create"
                    ) { _, _ ->
                        val text = txtName.text.toString()
                        if (text != ""){
                        Repository.instance.addTodoList(text)
                        binding.recyclerView.adapter = TodoListAdapter(it, Repository.instance.getTodoLists()) {
                            val action =
                                MainListFragmentDirections
                                    .actionMainListFragmentToToDoListFragment(todoList = it)
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

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
