package com.parinaz.todolist

import com.parinaz.todolist.domain.TodoList

object Repository {
    private val todoLists = mutableListOf(
        TodoList("shopping", 1),
        TodoList("personal", 2),
        TodoList("barcelona", 3)
    )

    fun getTodoLists(): List<TodoList>{
        return todoLists
    }

    fun addTodoList(name: String) {
        val todoList = TodoList(name, id = todoLists.maxOf { it.id } + 1)
        todoLists.add(todoList)
    }
}
