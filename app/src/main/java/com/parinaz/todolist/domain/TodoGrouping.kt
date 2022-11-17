package com.parinaz.todolist.domain

data class TodoGrouping(
    val title: String,
) {
    val id: Long = availableId--
    var isExpanded = false

    companion object {
        private var availableId = -1L
    }
}