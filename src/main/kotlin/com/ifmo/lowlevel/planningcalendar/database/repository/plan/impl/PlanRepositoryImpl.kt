package com.ifmo.lowlevel.planningcalendar.database.repository.plan.impl

import com.ifmo.lowlevel.planningcalendar.database.connection.ConnectionPoolHandler
import com.ifmo.lowlevel.planningcalendar.database.repository.plan.ifc.PlanRepository
import com.ifmo.lowlevel.planningcalendar.model.Plan
import com.ifmo.lowlevel.planningcalendar.model.PlanState
import com.ifmo.lowlevel.planningcalendar.model.handler.SecurityHandler
import com.ifmo.lowlevel.planningcalendar.utils.QueryUtils
import mu.KotlinLogging
import java.sql.*
import java.time.LocalDateTime
import java.time.ZoneOffset

object PlanRepositoryImpl : PlanRepository {

    private val logger = KotlinLogging.logger {}

    private val connectionPool = ConnectionPoolHandler.getPool()

    override fun findByDay(deadline: LocalDateTime): List<Plan> {
        val list = ArrayList<Plan>()
        val connection = connectionPool!!.getConnection()
        val statement = connection!!.createStatement()
        val set = statement.executeQuery(
            QueryUtils.selectWithConditions(
                Plan::class, "*",
                mapOf(Pair("deadline", deadline.toString()), Pair("plan_state", PlanState.DAY.name))
            )
        )
        while (set.next()) {
            list.add(getPlanFromResultSet(set))
        }
        connectionPool.releaseConnection(connection)

        logger.info { "Find ${list.size} Plans by $deadline" }
        return list
    }

    override fun findByMonth(deadline: LocalDateTime): List<Plan> {
        val list = ArrayList<Plan>()
        val connection = connectionPool!!.getConnection()
        val statement = connection!!.createStatement()
        val set = statement.executeQuery(
            QueryUtils.selectWithConditions(
                Plan::class, "*",
                mapOf(Pair("deadline", deadline.toString()), Pair("plan_state", PlanState.MONTH.name))
            )
        )
        while (set.next()) {
            list.add(getPlanFromResultSet(set))
        }
        connectionPool.releaseConnection(connection)
        logger.info { "Find ${list.size} Plans by $deadline" }
        return list
    }

    override fun findByYear(deadline: LocalDateTime): List<Plan> {
        val list = ArrayList<Plan>()
        val connection = connectionPool!!.getConnection()
        val statement = connection!!.createStatement()
        val set = statement.executeQuery(
            QueryUtils.selectWithConditions(
                Plan::class, "*",
                mapOf(Pair("deadline", deadline.toString()), Pair("plan_state", PlanState.YEAR.name))
            )
        )
        while (set.next()) {
            list.add(getPlanFromResultSet(set))
        }
        connectionPool.releaseConnection(connection)
        logger.info { "Find ${list.size} Plans by $deadline" }
        return list
    }

    override fun findAll(): List<Plan> {
        val list = ArrayList<Plan>()
        val connection = connectionPool!!.getConnection()
        val statement = connection!!.createStatement()
        val set = statement.executeQuery(
            QueryUtils.selectWithConditions(
                    Plan::class,
                    "*",
                    mapOf(Pair("user_login", SecurityHandler.currentUser().get()))
                )
        )
        while (set.next()) {
            list.add(getPlanFromResultSet(set))
        }
        connectionPool.releaseConnection(connection)

        logger.info { "Find ${list.size} Plans" }
        return list
    }

    override fun insert(plan: Plan) {
        val connection = connectionPool!!.getConnection()
        connection!!.autoCommit = false
        val preparedStatement = preparePlanStatement(connection, plan, QueryUtils.insert(Plan::class))
        preparedStatement.executeUpdate()
        connection.commit()

        logger.info { "Inserted Plan with id ${plan.getGoal()}" }
        connectionPool.releaseConnection(connection)
    }

    override fun update(goal: String, plan: Plan) {
        val connection = connectionPool!!.getConnection()
        connection!!.autoCommit = false
        val preparedStatement = preparePlanStatement(connection, plan, QueryUtils.update(Plan::class, "goal"))
        preparedStatement.setString(6, goal)
        preparedStatement.executeUpdate()
        connection.commit()

        logger.info { "Updated Plan with id $goal" }
        connectionPool.releaseConnection(connection)
    }

    override fun delete(goal: String) {
        val connection = connectionPool!!.getConnection()
        connection!!.autoCommit = false
        val statement = connection.createStatement()
        statement.executeQuery(QueryUtils.delete(Plan::class, mapOf(Pair("goal", goal))))
        connection.commit()

        logger.info { "Deleted Plan with id $goal" }
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
        preparedStatement.setTimestamp(
            4,
            Timestamp(plan.getDeadline()!!.toInstant(ZoneOffset.UTC).toEpochMilli())
        )
        preparedStatement.setString(5, plan.getPlanState().name)
        preparedStatement.setString(6, plan.getUserLogin())
        return preparedStatement
    }

    private fun getPlanFromResultSet(set: ResultSet): Plan {
        return Plan
            .goal(set.getString("goal"))
            .completed(set.getBoolean("completed"))
            .description(set.getString("description"))
            .deadline(LocalDateTime.ofInstant(set.getTimestamp("deadline").toInstant(), ZoneOffset.UTC))
            .planState(PlanState.valueOf(set.getString("plan_state").uppercase()))
            .userLogin(set.getString("user_login"))
            .build()
    }
}