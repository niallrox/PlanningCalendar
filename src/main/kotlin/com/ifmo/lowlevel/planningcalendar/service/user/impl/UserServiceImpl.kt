package com.ifmo.lowlevel.planningcalendar.service.user.impl

import at.favre.lib.crypto.bcrypt.BCrypt
import com.ifmo.lowlevel.planningcalendar.database.repository.user.ifc.UserRepository
import com.ifmo.lowlevel.planningcalendar.model.common.SysConsts
import com.ifmo.lowlevel.planningcalendar.service.user.ifc.UserService
import java.lang.IllegalArgumentException
import java.util.*

class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override fun login(login: String, password: String): Boolean {
        if (Objects.nonNull(login) && Objects.nonNull(password)) {
            return BCrypt.verifyer().verify(password.toCharArray(), userRepository.hashByLogin(login)).verified
        } else {
            throw IllegalArgumentException("login and password can't be null")
        }
    }

    override fun register(login: String, password: String): Boolean {
        if (Objects.nonNull(login) && Objects.nonNull(password)) {
            val hash = BCrypt.withDefaults().hashToString(SysConsts.HASH_COST_FACTOR, password.toCharArray())
            return userRepository.register(login, hash)
        } else {
            throw IllegalArgumentException("login and password can't be null")
        }
    }
}