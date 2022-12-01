package com.parinaz.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.parinaz.todolist.adapter.TodoListAdapter
import com.parinaz.todolist.databinding.FragmentListOfTodoListBinding

class ListOfTodoListFragment : Fragment() {

    private var _binding: FragmentListOfTodoListBinding? = null
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
        _binding = FragmentListOfTodoListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.title = getString(R.string.app_name)

        navigateToTodoList()

        createNewList()
    }

    private fun navigateToTodoList() {
        val toDoLists = Repository.instance.getTodoLists()
        val adapter = TodoListAdapter(requireContext(), toDoLists) {
            val action =
                ListOfTodoListFragmentDirections
                    .actionMainListFragmentToToDoListFragment(it.id)
            view?.findNavController()?.navigate(action)
        }
        binding.recyclerViewTodoList.adapter = adapter
    }

    private fun createNewList() {
        binding.addNewList.setOnClickListener(){
            context?.let {
                val txtName =  EditText(it)
                txtName.hint = "Enter list title"

                AlertDialog.Builder(it)
                    .setTitle("New list")
                    .setMessage("Pleas enter the new list name")
                    .setView(txtName)
                    .setPositiveButton("CREATE LIST"
                    ) { _, _ ->
                        val text = txtName.text.toString()
                        if (text != ""){
                            Repository.instance.addTodoList(text)
                            binding.recyclerViewTodoList.adapter = TodoListAdapter(it, Repository.instance.getTodoLists()) {
                                val action =
                                    ListOfTodoListFragmentDirections
                                        .actionMainListFragmentToToDoListFragment(it.id)
                                view?.findNavController()?.navigate(action)
                            }
                        }else{
                            Toast.makeText(context, "Name can not be empty", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("CANCEL"
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
