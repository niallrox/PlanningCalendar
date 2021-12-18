package com.ifmo.lowlevel.planningcalendar.database.connection

import java.sql.Connection

interface ConnectionPool {
    fun getConnection(): Connection?
    fun releaseConnection(connection: Connection?): Boolean
}