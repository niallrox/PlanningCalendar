package com.ifmo.lowlevel.planningcalendar.database.repository.user.ifc

interface UserRepository {
    fun login(login: String, password: String): Boolean
    fun register(login: String, password: String): Boolean
}