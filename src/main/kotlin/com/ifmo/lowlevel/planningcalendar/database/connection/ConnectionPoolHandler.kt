package com.ifmo.lowlevel.planningcalendar.database.connection

import com.ifmo.lowlevel.planningcalendar.ApplicationEnvironment
import com.mysql.cj.jdbc.Driver
import mu.KotlinLogging
import java.sql.DriverManager
import java.util.*

class ConnectionPoolHandler {

    companion object {
        private val logger = KotlinLogging.logger {}
        private val properties = getProperty()
        private val url = properties.getProperty("url")
        private val user = properties.getProperty("user")
        private val password = properties.getProperty("password")

        init {
            DriverManager.registerDriver(Driver())
        }


        private fun getProperty() = ApplicationEnvironment.getPropertiesByName("connection")

        @Volatile
        var connectionPoolImpl: ConnectionPool? = null
        private val any: Any = Any()

        fun getPool() : ConnectionPool? {
            if (Objects.isNull(connectionPoolImpl)) {
                synchronized(any) {
                    if (Objects.isNull(connectionPoolImpl)) {
                        logger.debug { "Creating connection pool..." }
                        connectionPoolImpl = ConnectionPoolImpl.create(url, user, password)
                        logger.debug { "Connection pool created." }
                    }
                }
            }
            return connectionPoolImpl
        }

    }
}