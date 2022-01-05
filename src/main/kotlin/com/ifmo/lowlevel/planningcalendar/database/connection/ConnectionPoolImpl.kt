package com.ifmo.lowlevel.planningcalendar.database.connection

import mu.KotlinLogging
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


class ConnectionPoolImpl private constructor(list: MutableList<Connection?>) : ConnectionPool{
    private val connectionPool: MutableList<Connection?> = list
    private val usedConnections: MutableList<Connection?> = ArrayList()


   companion object {
       private val logger = KotlinLogging.logger {}

       private const val INITIAL_POOL_SIZE = 10

       private fun createConnection(
           url: String, user: String, password: String
       ): Connection {
           return DriverManager.getConnection(url, user, password)
       }

       fun create(
           url: String, user: String,
           password: String
       ): ConnectionPoolImpl {
           val pool: MutableList<Connection?> = ArrayList(INITIAL_POOL_SIZE)
           for (i in 0 until INITIAL_POOL_SIZE) {
               pool.add(createConnection(url, user, password))
           }
           logger.trace { "Connection pool initialized" }
           return ConnectionPoolImpl(pool)
       }
   }
    override fun getConnection(): Connection? {
        val connection: Connection? = connectionPool
            .removeAt(connectionPool.size - 1)
        usedConnections.add(connection)
        return connection
    }

    override fun releaseConnection(connection: Connection?): Boolean {
        connectionPool.add(connection)
        return usedConnections.remove(connection)
    }

    fun getSize(): Int {
        return connectionPool.size + usedConnections.size
    }
}