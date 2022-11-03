package com.parinaz.todolist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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
}
