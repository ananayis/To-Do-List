package com.parinaz.todolist

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.initInstance(this)
    }
}