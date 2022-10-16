package com.parinaz.todolist

import com.parinaz.todolist.domain.Todo
import com.parinaz.todolist.domain.TodoList

object Repository {
    private val todoLists = mutableListOf(
        TodoList("shopping", 1),
        TodoList("personal", 2),
        TodoList("barcelona", 3)
    )

    private val todos = mutableListOf(
        Todo("bread",1,1),
        Todo("water",2,1) ,
        Todo("nail",3,1),
        Todo("hair",4,1),
        Todo("post office",5,3),
        Todo("pharmacy",6,3)
    )

    fun getTodoLists(): List<TodoList>{
        return todoLists
    }

    fun addTodoList(name: String) {
        val todoList = TodoList(name, id = todoLists.maxOf { it.id } + 1)
        todoLists.add(todoList)
    }

    fun getTodos(todoListId: Long): List<Todo>{
        /*val selectedList = mutableListOf<Todo>()

        for (item in todos) {
            if(todoListId == item.todoListId){
                selectedList.add(item)
            }
        }
        return selectedList*/
        return todos.filter { it.todoListId == todoListId }
    }

    fun addTodo(name: String, todoListId: Long) {
        val todo = Todo(name, id = todos.maxOf { it.id } + 1, todoListId)
        todos.add(todo)
    }
}
