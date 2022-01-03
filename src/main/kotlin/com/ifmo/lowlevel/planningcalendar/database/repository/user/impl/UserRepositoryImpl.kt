package com.ifmo.lowlevel.planningcalendar.database.repository.user.impl

import com.ifmo.lowlevel.planningcalendar.database.connection.ConnectionPoolHandler
import com.ifmo.lowlevel.planningcalendar.database.repository.plan.impl.PlanRepositoryImpl
import com.ifmo.lowlevel.planningcalendar.database.repository.user.ifc.UserRepository
import com.ifmo.lowlevel.planningcalendar.model.Plan
import com.ifmo.lowlevel.planningcalendar.model.PlanState
import com.ifmo.lowlevel.planningcalendar.model.User
import com.ifmo.lowlevel.planningcalendar.utils.QueryUtils
import mu.KotlinLogging
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

object UserRepositoryImpl : UserRepository {

    private val logger = KotlinLogging.logger {}

    private val connectionPool = ConnectionPoolHandler.getPool()

    override fun hashByLogin(login: String): String {
        val connection = connectionPool!!.getConnection()
        return try {
            val statement = connection!!.createStatement()
            val set = statement.executeQuery(
                QueryUtils.select(
                    User::class,
                    "password"
                )
            )
            set.next()
            connectionPool.releaseConnection(connection)
            set.getString("password")
        } catch (e: SQLException) {
            logger.error { "Something went wrong with statement. Error: $e" }
            ""
        }
    }

    override fun register(login: String, password: String): Boolean {
        val connection = connectionPool!!.getConnection()
        return try {
            val prepareUserStatement = prepareUserStatement(connection, login, password)
            prepareUserStatement.executeUpdate()
            true
        } catch (e: SQLException) {
            logger.error { "Something went wrong with statement. Error: $e" }
            false
        }
    }

    private fun prepareUserStatement(connection: Connection?, login: String, password: String) : PreparedStatement {
        val prepareStatement = connection!!.prepareStatement(QueryUtils.insert(User::class))
        prepareStatement.setString(1, login)
        prepareStatement.setString(2, password)
        return prepareStatement
    }
}