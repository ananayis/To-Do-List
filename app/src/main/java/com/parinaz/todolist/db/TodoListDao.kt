package com.parinaz.todolist.db

import androidx.room.*
import com.parinaz.todolist.domain.TodoList

@Dao
interface TodoListDao {

    @Query("SELECT * FROM todo_list;")
    fun getAll(): List<TodoList>

    @Query("SELECT COUNT(*) FROM todo WHERE :todoListId = todo_list_id AND done = 0;")
    fun getUnDoneTodoNumber(todoListId: Long): Int

    @Insert
    fun addTodoList(todoList: TodoList): Long

    @Update
    fun updateTodoList(todoList: TodoList)

    @Query("SELECT * FROM todo_list WHERE :id = id;")
    fun getTodoList(id: Long): TodoList
}
