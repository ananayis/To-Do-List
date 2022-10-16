package com.parinaz.todolist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val todoViewHolder = TodoViewHolder(view)
        return todoViewHolder
    }

    override fun onBindViewHolder(holder: TodoAdapter.TodoViewHolder, position: Int) {
        val todo = dataSet[position]
        holder.binding.name.text = todo.name
//        holder.id = item.id
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = TodosBinding.bind(view)
        var id: Long = 0

        init {
            binding.root.setOnClickListener {
                clickListener(id)
            }

        }
    }
}