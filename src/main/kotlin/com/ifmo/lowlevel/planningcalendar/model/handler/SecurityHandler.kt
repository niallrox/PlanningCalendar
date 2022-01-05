package com.ifmo.lowlevel.planningcalendar.model.handler

import java.util.*

object SecurityHandler {

    private lateinit var login: String

    fun setUser(login: String) = run { this.login = login }

    fun currentUser(): Optional<String> = Optional.ofNullable(login)
}