package com.ifmo.lowlevel.planningcalendar.database.repository.impl

import com.ifmo.lowlevel.planningcalendar.database.connection.ConnectionPoolHandler
import com.ifmo.lowlevel.planningcalendar.database.repository.ifc.PlanRepository
import com.ifmo.lowlevel.planningcalendar.model.Plan
import com.ifmo.lowlevel.planningcalendar.model.PlanState
import com.ifmo.lowlevel.planningcalendar.utils.QueryUtils
import mu.KotlinLogging
import java.sql.*
import java.time.LocalDateTime
import java.time.ZoneOffset

class PlanRepositoryImpl : PlanRepository {

    private val logger = KotlinLogging.logger {}

    private val connectionPool = ConnectionPoolHandler.getPool()

    override fun findByDay(goal: String): List<Plan> {
        val list = ArrayList<Plan>()
        val connection = connectionPool!!.getConnection()
        try {
            val statement = connection!!.createStatement()
            val set = statement.executeQuery(
                QueryUtils.select(
                    Plan::class, "*",
                    mapOf(Pair("goal", goal), Pair("plan_state", PlanState.DAY.name))
                )
            )
            while (set.next()) {
                list.add(getPlanFromResultSet(set))
            }
            connectionPool.releaseConnection(connection)
        } catch (e: SQLException) {
            logger.error { String.format("Something went wrong with statement. Error: %s ", e) }
        }
        return list
    }

    override fun findByMonth(goal: String): List<Plan> {
        val list = ArrayList<Plan>()
        val connection = connectionPool!!.getConnection()
        try {
            val statement = connection!!.createStatement()
            val set = statement.executeQuery(
                QueryUtils.select(
                    Plan::class, "*",
                    mapOf(Pair("goal", goal), Pair("plan_state", PlanState.MONTH.name))
                )
            )
            while (set.next()) {
                list.add(getPlanFromResultSet(set))
            }
            connectionPool.releaseConnection(connection)
        } catch (e: SQLException) {
            logger.error { String.format("Something went wrong with statement. Error: %s ", e) }
        }
        return list
    }

    override fun findByYear(goal: String): List<Plan> {
        val list = ArrayList<Plan>()
        val connection = connectionPool!!.getConnection()
        try {
            val statement = connection!!.createStatement()
            val set = statement.executeQuery(
                QueryUtils.select(
                    Plan::class, "*",
                    mapOf(Pair("goal", goal), Pair("plan_state", PlanState.YEAR.name))
                )
            )
            while (set.next()) {
                list.add(getPlanFromResultSet(set))
            }
            connectionPool.releaseConnection(connection)
        } catch (e: SQLException) {
            logger.error { String.format("Something went wrong with statement. Error: %s ", e) }
        }
        return list
    }

    override fun insert(plan: Plan) {
        val connection = connectionPool!!.getConnection()
        connection!!.autoCommit = false
        try {
            val preparedStatement = preparePlanStatement(connection, plan, QueryUtils.insert(Plan::class))
            preparedStatement.executeUpdate()
            connection.commit()
        } catch (e: SQLException) {
            logger.error { String.format("Something went wrong with statement. Error: %s ", e) }
            connection.rollback()
        }
        connectionPool.releaseConnection(connection)
    }

    override fun update(goal: String, plan: Plan) {
        val connection = connectionPool!!.getConnection()
        connection!!.autoCommit = false
        try {
            val preparedStatement = preparePlanStatement(connection, plan, QueryUtils.update(Plan::class, "goal"))
            preparedStatement.setString(6, goal)
            preparedStatement.executeUpdate()
            connection.commit()
        } catch (e: SQLException) {
            logger.error { String.format("Something went wrong with statement. Error: %s ", e) }
            connection.rollback()
        }
        connectionPool.releaseConnection(connection)
    }

    override fun delete(goal: String) {
        val connection = connectionPool!!.getConnection()
        connection!!.autoCommit = false
        try {
            val statement = connection.createStatement()
            statement.executeQuery(QueryUtils.delete(Plan::class, mapOf(Pair("goal", goal))))
            connection.commit()
        } catch (e: SQLException) {
            logger.error { String.format("Something went wrong with statement. Error: %s ", e) }
            connection.rollback()
        }
        connectionPool.releaseConnection(connection)
    }

    private fun preparePlanStatement(
        connection: Connection,
        plan: Plan,
        query: String
    ): PreparedStatement {
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setString(1, plan.getGoal())
        preparedStatement.setBoolean(2, plan.isCompleted())
        preparedStatement.setString(3, plan.getDescription())
        preparedStatement.setDate(
            4,
            Date(plan.getDeadline()!!.toInstant(ZoneOffset.UTC).toEpochMilli())
        )
        preparedStatement.setString(5, plan.getPlanState().name)
        return preparedStatement
    }

    private fun getPlanFromResultSet(set: ResultSet): Plan {
        return Plan
            .goal(set.getString("goal"))
            .completed(set.getBoolean("completed"))
            .description(set.getString("description"))
            .deadline(LocalDateTime.ofInstant(set.getDate("deadline").toInstant(), ZoneOffset.UTC))
            .planState(PlanState.valueOf(set.getString("plan_state").uppercase()))
            .build()
    }
}