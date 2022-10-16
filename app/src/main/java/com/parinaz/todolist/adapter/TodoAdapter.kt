package com.parinaz.todolist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.parinaz.todolist.R
import com.parinaz.todolist.databinding.TodosBinding
import com.parinaz.todolist.domain.Todo
import com.parinaz.todolist.domain.TodoList

class TodoAdapter (private val context: Context,
                   private val dataSet: List<Todo>,
                   private val clickListener: (Long) -> Unit,
        ) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.TodoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.todos, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoAdapter.TodoViewHolder, position: Int) {
        val todo = dataSet[position]
        holder.binding.root.setOnClickListener {
            clickListener(todo.id)
        }

        holder.binding.name.text = todo.name
        holder.binding.checked.setOnCheckedChangeListener(null)
        holder.binding.checked.isChecked = todo.done
        holder.binding.checked.setOnCheckedChangeListener { _, isChecked ->
            todo.done = isChecked
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = TodosBinding.bind(view)
    }
}
