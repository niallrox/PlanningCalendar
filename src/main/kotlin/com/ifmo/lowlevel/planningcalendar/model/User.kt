package com.ifmo.lowlevel.planningcalendar.model

class User (private val login: String, private val password: String) {
    fun getGoal() = login
    fun getDescription() = password
}