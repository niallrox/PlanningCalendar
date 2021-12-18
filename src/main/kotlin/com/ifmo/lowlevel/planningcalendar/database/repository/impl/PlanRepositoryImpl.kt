package com.ifmo.lowlevel.planningcalendar.database.repository.impl

import com.ifmo.lowlevel.planningcalendar.database.connection.ConnectionPool
import com.ifmo.lowlevel.planningcalendar.database.connection.ConnectionPoolHandler
import com.ifmo.lowlevel.planningcalendar.database.repository.ifc.PlanRepository
import com.ifmo.lowlevel.planningcalendar.model.Plan

class PlanRepositoryImpl: PlanRepository {

    private val connectionPool = ConnectionPoolHandler.getPool()

    override fun findByDay(goal: String): List<Plan> {
        val connection = connectionPool!!.getConnection()
        TODO("Not yet implemented")
        connectionPool.releaseConnection(connection)
    }

    override fun findByMonth(goal: String): List<Plan> {
        val connection = connectionPool!!.getConnection()
        TODO("Not yet implemented")
        connectionPool.releaseConnection(connection)    }

    override fun findByYear(goal: String): List<Plan> {
        val connection = connectionPool!!.getConnection()
        TODO("Not yet implemented")
        connectionPool.releaseConnection(connection)    }

    override fun insert(plan: Plan) {
        val connection = connectionPool!!.getConnection()
        TODO("Not yet implemented")
        connectionPool.releaseConnection(connection)    }

    override fun update(goal: String, plan: Plan) {
        val connection = connectionPool!!.getConnection()
        TODO("Not yet implemented")
        connectionPool.releaseConnection(connection)    }

    override fun delete(goal: String) {
        val connection = connectionPool!!.getConnection()
        TODO("Not yet implemented")
        connectionPool.releaseConnection(connection)    }
}