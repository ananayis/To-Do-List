package com.parinaz.todolist.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.parinaz.todolist.domain.TodoList

@Dao
interface TodoListDao {

    @Query("SELECT * FROM todo_list;")
    fun getAll() : List<TodoList>

    @Query("SELECT COUNT(*) FROM todo WHERE :todoListId = todo_list_id AND done = 0;")
    fun getUnDoneTodoNumber(todoListId: Long) : Int

    @Insert
    fun addTodoList(todoList: TodoList): Long
}
