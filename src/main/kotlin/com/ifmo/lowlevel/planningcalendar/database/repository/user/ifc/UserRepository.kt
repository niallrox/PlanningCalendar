package com.ifmo.lowlevel.planningcalendar.database.repository.user.ifc

interface UserRepository {
    fun hashByLogin(login: String): String
    fun register(login: String, password: String): Boolean
}