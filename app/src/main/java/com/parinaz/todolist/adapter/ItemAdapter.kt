package com.parinaz.todolist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parinaz.todolist.MainActivity
import com.parinaz.todolist.R
import com.parinaz.todolist.ToDoListFragment
import com.parinaz.todolist.databinding.FragmentToDoListBinding
import com.parinaz.todolist.databinding.ItemBinding
import com.parinaz.todolist.domain.TodoList

class ItemAdapter(private val context: Context,
                  private val dataSet: List<TodoList>
                  ) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        val itemViewHolder = ItemViewHolder(view)
        return itemViewHolder
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataSet[position]
        holder.binding.name.text = item.name
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemBinding.bind(view)
    }

}