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

    fun idToday(date: Date): Boolean {
        val c = Calendar.getInstance()
        c.timeInMillis = System.currentTimeMillis()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        c.time = date
        val y = c.get(Calendar.YEAR)
        val m = c.get(Calendar.MONTH)
        val d = c.get(Calendar.DAY_OF_MONTH)

        return y == year && m == month && d == day
    }
}