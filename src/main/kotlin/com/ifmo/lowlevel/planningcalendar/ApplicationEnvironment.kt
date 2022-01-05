package com.ifmo.lowlevel.planningcalendar

import com.ifmo.lowlevel.planningcalendar.database.repository.plan.impl.PlanRepositoryImpl
import com.ifmo.lowlevel.planningcalendar.database.repository.user.impl.UserRepositoryImpl
import com.ifmo.lowlevel.planningcalendar.ext.google.calendar.CalendarHandler
import com.ifmo.lowlevel.planningcalendar.service.plan.ifc.PlanService
import com.ifmo.lowlevel.planningcalendar.service.plan.impl.PlanServiceImpl
import com.ifmo.lowlevel.planningcalendar.service.user.ifc.UserService
import com.ifmo.lowlevel.planningcalendar.service.user.impl.UserServiceImpl
import java.io.FileInputStream
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.HashSet

object ApplicationEnvironment {

    private val properties = Properties()

    init {
        val fileInputStream = FileInputStream("src/main/resources/url-handler.properties")
        properties.load(fileInputStream)
    }

    fun getPropertiesByName(string: String): Properties {
        val url = properties.getProperty(string)
        val fileInputStream = FileInputStream(url)
        val propertiesByKey = Properties()
        propertiesByKey.load(fileInputStream)
        return propertiesByKey
    }

    fun planService(): PlanService = PlanServiceImpl(CalendarHandler, PlanRepositoryImpl)

    fun userService(): UserService = UserServiceImpl(UserRepositoryImpl)
}