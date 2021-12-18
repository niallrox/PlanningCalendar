package com.ifmo.lowlevel.planningcalendar

import com.ifmo.lowlevel.planningcalendar.database.connection.ConnectionPoolHandler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SingletonTest {

    @Test
    fun testSingletons() {
        assertEquals(ConnectionPoolHandler.getPool(), ConnectionPoolHandler.getPool())
    }
}