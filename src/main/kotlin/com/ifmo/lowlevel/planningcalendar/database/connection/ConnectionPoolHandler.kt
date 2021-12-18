package com.ifmo.lowlevel.planningcalendar.database.connection

import com.ifmo.lowlevel.planningcalendar.Application
import java.util.*

class ConnectionPoolHandler {

    companion object {
        private val properties = getProperty()
        private val url = properties.getProperty("url")
        private val user = properties.getProperty("user")
        private val password = properties.getProperty("password")


        private fun getProperty() = Application.getUrlByPropertyHandler("connection")

        @Volatile
        var connectionPoolImpl: ConnectionPool? = null

        fun getPool() : ConnectionPool? {
            if (Objects.isNull(connectionPoolImpl)) {
                connectionPoolImpl?.let {
                    synchronized(it) {
                        if (Objects.isNull(connectionPoolImpl)) {
                            connectionPoolImpl = ConnectionPoolImpl.create(url, user, password)
                        }
                    }
                }
            }
            return connectionPoolImpl
        }

    }
}