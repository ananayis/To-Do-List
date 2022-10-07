package com.parinaz.todolist

import com.parinaz.todolist.domain.TodoList

object Repository {
    fun getTodoLists(): List<TodoList>{
        return listOf(
            TodoList("shopping", 1),
            TodoList("personal", 2),
            TodoList("barcelona", 3)
        )
    }
}