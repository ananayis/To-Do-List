package com.parinaz.todolist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.parinaz.todolist.databinding.FragmentNoteBinding
import com.parinaz.todolist.databinding.FragmentTodoBinding

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private val args: NoteFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("ananayis", "back pressed")
                val newTodo = args.todo.copy(note = binding.txtNote.text.toString())
                Repository.instance.updateTodo(newTodo)
                isEnabled = false
                requireActivity().onBackPressed()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.title = args.todo.name
        binding.txtNote.setText(args.todo.note)
        binding.txtNote.requestFocus()
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
