package com.parinaz.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parinaz.todolist.databinding.FragmentMainListBinding
import android.R
import android.app.Notification
import android.content.DialogInterface
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.parinaz.todolist.adapter.ItemAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainListFragment : Fragment() {

    private var _binding: FragmentMainListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toDoLists = Repository.getTodoLists()
        val adapter = ItemAdapter(requireContext(), toDoLists) {

            val action =
                MainListFragmentDirections
                    .actionMainListFragmentToToDoListFragment(todoListId = 1)
            view.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter

        binding.button.setOnClickListener(){

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
                        Repository.addTodoList(text)
                        binding.recyclerView.adapter = ItemAdapter(it, Repository.getTodoLists()) {
//            findNavController().navigate
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
