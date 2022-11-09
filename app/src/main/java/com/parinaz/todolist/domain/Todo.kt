package com.parinaz.todolist.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "todo", foreignKeys = [ForeignKey(
    entity = TodoList::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("todo_list_id"),
    onDelete = ForeignKey.CASCADE
)])
data class Todo(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "todo_list_id") val todoListId: Long,
    @ColumnInfo(name = "done") val done: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    ) : Serializable
