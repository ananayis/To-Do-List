package com.parinaz.todolist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.parinaz.todolist.domain.Todo
import com.parinaz.todolist.domain.TodoList

@Database(entities = [TodoList::class, Todo::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoListDao(): TodoListDao
    abstract fun todoDao(): TodoDao
}
