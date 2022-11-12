package com.parinaz.todolist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.parinaz.todolist.DateUtils
import com.parinaz.todolist.R
import com.parinaz.todolist.Repository
import com.parinaz.todolist.TodoFragment
import com.parinaz.todolist.databinding.TodosBinding
import com.parinaz.todolist.domain.Todo
import com.parinaz.todolist.domain.TodoList
import java.util.*

class TodoAdapter(
    private val context: Context,
    private val todoListId: Long,
    private val clickListener: (Todo) -> Unit,
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private lateinit var dataSet: List<Todo>

    init {
        setData(Repository.instance.getTodos(todoListId))
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return dataSet[position].id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.TodoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.todos, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoAdapter.TodoViewHolder, position: Int) {
        val todo = dataSet[position]
        holder.binding.root.setOnClickListener {
            clickListener(todo)
        }

        holder.binding.name.text = todo.name
        holder.binding.checked.setOnCheckedChangeListener(null)
        holder.binding.checked.isChecked = todo.done
        holder.binding.checked.setOnCheckedChangeListener { _, isChecked ->
            val newTodo = todo.copy(done = isChecked)
            Repository.instance.updateTodo(newTodo)
            setData(Repository.instance.getTodos(todoListId))
            notifyDataSetChanged()
        }
        if (todo.important) {
            holder.binding.star.setImageResource(R.drawable.ic_full_star_24)
        }else{
            holder.binding.star.setImageResource(R.drawable.ic_empty_star_24)
        }

        val dueDate = todo.dueDate
        if (dueDate != null) {
            val c = Calendar.getInstance()
            c.time = dueDate
            holder.binding.txtDueDate.text = "${c.get(Calendar.YEAR)}/${c.get(Calendar.MONTH)+1}/${c.get(Calendar.DAY_OF_MONTH)}"
            if (DateUtils.isPastDay(dueDate)){
                holder.binding.txtDueDate.setTextColor(ContextCompat.getColor(context, R.color.red))
                holder.binding.imgCalender.isVisible = true
            } else if (DateUtils.idToday(dueDate)) {
                holder.binding.txtDueDate.setTextColor(ContextCompat.getColor(context, R.color.purple_500))
                holder.binding.imgCalender.isVisible = true
            } else {
                holder.binding.txtDueDate.setTextColor(ContextCompat.getColor(context, R.color.gray))
                holder.binding.imgCalender.isVisible = true
            }
        } else {
            holder.binding.txtDueDate.text = ""
        }

        holder.binding.star.setOnClickListener { view ->
            val newImportant = !todo.important
            if (newImportant) {
                holder.binding.star.setImageResource(R.drawable.ic_full_star_24)
            }else{
                holder.binding.star.setImageResource(R.drawable.ic_empty_star_24)
            }
            val newTodo = todo.copy(important = newImportant)
            Repository.instance.updateTodo(newTodo)
            setData(Repository.instance.getTodos(todoListId))
            notifyDataSetChanged()
        }
    }

    private fun setData(todos: List<Todo>) {
        dataSet = todos.sortedBy { !it.important }.sortedBy { it.done }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = TodosBinding.bind(view)
    }
}
