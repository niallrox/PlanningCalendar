package com.ifmo.lowlevel.planningcalendar.service.user.ifc

interface UserService {
    fun login(login: String, password: String): Boolean
    fun register(login: String, password: String): Boolean
}