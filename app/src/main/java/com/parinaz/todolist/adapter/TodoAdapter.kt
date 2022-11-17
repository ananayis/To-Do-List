package com.parinaz.todolist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.parinaz.todolist.DateUtils
import com.parinaz.todolist.R
import com.parinaz.todolist.Repository
import com.parinaz.todolist.databinding.ItemTodoBinding
import com.parinaz.todolist.databinding.ItemTodoGroupingBinding
import com.parinaz.todolist.domain.Todo
import com.parinaz.todolist.domain.TodoGrouping
import java.lang.IllegalStateException
import java.util.*

class TodoAdapter(
    private val context: Context,
    private val todoListId: Long,
    private val clickListener: (Todo) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var dataSet: List<Any>
    private  val completed = TodoGrouping("Completed")

    init {
        loadData()
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        val item = dataSet[position]
        if (item is Todo) {
            return item.id
        } else if (item is TodoGrouping) {
            return item.id
        } else {
            throw IllegalStateException("wrong item type: ${item.javaClass.simpleName}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = dataSet[position]
        if (item is Todo) {
            return 1
        } else if (item is TodoGrouping) {
            return 2
        } else {
            throw IllegalStateException("wrong item type: ${item.javaClass.simpleName}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false)
            return TodoViewHolder(view)
        } else if (viewType == 2) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_todo_grouping, parent, false)
            return TodoGroupingViewHolder(view)
        } else {
            throw IllegalStateException("wrong view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataSet[position]
        if (holder is TodoViewHolder) {
            val todo = item as Todo
            holder.binding.root.setOnClickListener {
                clickListener(todo)
            }
            holder.binding.name.text = todo.name
            holder.binding.checked.setOnCheckedChangeListener(null)
            holder.binding.checked.isChecked = todo.done
            holder.binding.checked.setOnCheckedChangeListener { _, isChecked ->
                val newTodo = todo.copy(done = isChecked)
                Repository.instance.updateTodo(newTodo)
                loadData()
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
                } else if (DateUtils.idToday(dueDate)) {
                    holder.binding.txtDueDate.setTextColor(ContextCompat.getColor(context, R.color.purple_500))
                } else {
                    holder.binding.txtDueDate.setTextColor(ContextCompat.getColor(context, R.color.gray))
                }
                holder.binding.imgCalender.isVisible = true
                holder.binding.txtDueDate.isVisible = true
            } else {
                holder.binding.imgCalender.isVisible = false
                holder.binding.txtDueDate.isVisible = false
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
                loadData()
                notifyDataSetChanged()
            }

            holder.binding.imgNote.isVisible = todo.note.isNotEmpty()

        } else if (holder is TodoGroupingViewHolder) {
            val todoGrouping = item as TodoGrouping
            holder.binding.title.text = todoGrouping.title

            holder.binding.root.setOnClickListener {
                todoGrouping.isExpanded = !todoGrouping.isExpanded
                loadData()
                notifyDataSetChanged()
            }
        } else {
            throw IllegalStateException("wrong holder type: ${holder.javaClass.simpleName}")
        }
    }

    private fun loadData() {
        val todos = Repository.instance.getTodos(todoListId)
        val resultList = mutableListOf<Any>()
        resultList.addAll(todos.filter { !it.done })
        val doneTodos = todos.filter { it.done }
        if (doneTodos.isNotEmpty()) {
            resultList.add(completed)
            if (completed.isExpanded) {
                resultList.addAll(doneTodos)
            }
        }
        dataSet = resultList
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTodoBinding.bind(view)
    }

    inner class TodoGroupingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTodoGroupingBinding.bind(view)
    }
}
