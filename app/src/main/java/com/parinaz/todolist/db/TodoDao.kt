package com.parinaz.todolist.db

import androidx.room.*
import com.parinaz.todolist.domain.Todo

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo WHERE :todoListId = todo_list_id;")
    fun getTodosInList(todoListId: Long): List<Todo>

    @Insert
    fun addTodo(todo: Todo): Long

    @Update
    fun updateTodo(todo: Todo)

    @Query("DELETE FROM todo_list WHERE :todoListId = id;" )
    fun deleteTodoList(todoListId: Long)

    @Delete
    fun deleteTodo(todo: Todo)

    @Query("SELECT * FROM TODO WHERE :id = id;")
    fun getTodo(id: Long): Todo

    @Query("SELECT COUNT(*) FROM todo WHERE :todoListId = todo_list_id AND done = 1;")
    fun getDoneTodoNumber(todoListId: Long) : Int
}
