package com.parinaz.todolist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parinaz.todolist.R
import com.parinaz.todolist.Repository
import com.parinaz.todolist.databinding.ItemTodoListBinding
import com.parinaz.todolist.domain.TodoList

class TodoListAdapter(
    private val context: Context,
    private val dataSet: List<TodoList>,
    private val clickListener: (TodoList) -> Unit,
    ) : RecyclerView.Adapter<TodoListAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_todo_list, parent, false)
        val itemViewHolder = ItemViewHolder(view)
        return itemViewHolder
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataSet[position]
        holder.binding.name.text = item.name
        val count = Repository.instance.countTodos(item.id)
        if (count != 0) {
            holder.binding.number.text = count.toString()
        }else{
            holder.binding.number.text = ""
        }
        holder.binding.root.setOnClickListener {
            clickListener(item)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTodoListBinding.bind(view)
    }
}
