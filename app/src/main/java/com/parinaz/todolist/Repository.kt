package com.parinaz.todolist

import android.content.Context
import androidx.room.Room
import com.parinaz.todolist.db.AppDatabase
import com.parinaz.todolist.domain.Todo
import com.parinaz.todolist.domain.TodoList

class Repository private constructor(context: Context) {

    private val db = Room.databaseBuilder(
        context, AppDatabase::class.java, "database"
    ).allowMainThreadQueries().build()

    fun getTodoLists(): List<TodoList>{
        return db.todoListDao().getAll()
    }

    fun addTodoList(name: String) {
        val todoList = TodoList(name)
        db.todoListDao().addTodoList(todoList)
    }

    fun getTodos(todoListId: Long): List<Todo>{
        return db.todoDao().getTodosInList(todoListId)
    }

    fun addTodo(name: String, todoListId: Long) {
        val todo = Todo(name, todoListId)
        db.todoDao().addTodo(todo)
    }

    fun countTodos(todoListId: Long): Int {
        return db.todoListDao().getUnDoneTodoNumber(todoListId)
    }

    fun updateTodo(todo: Todo) {
        db.todoDao().updateTodo(todo)
    }

    fun deleteTodoList(todoListId: Long) {
        db.todoDao().deleteTodoList(todoListId)
    }

    fun deleteTodo(todo: Todo){
        db.todoDao().deleteTodo(todo)
    }

    companion object {
        private var _instance: Repository? = null
        val instance: Repository
            get() = _instance!!

        fun initInstance(context: Context){
            if (_instance == null) {
                _instance = Repository(context)
            }
        }
    }
}
