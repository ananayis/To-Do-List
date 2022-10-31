package com.parinaz.todolist.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class Todo(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "todo_list_id") val todoListId: Long,
    @ColumnInfo(name = "done") val done: Boolean = false,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
)