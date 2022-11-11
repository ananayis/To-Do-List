package com.parinaz.todolist

import java.util.*

object DateUtils {
    fun isPastDay(date: Date): Boolean {
        val c = Calendar.getInstance()
        c.timeInMillis = System.currentTimeMillis()
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return date.time < c.timeInMillis
    }
}