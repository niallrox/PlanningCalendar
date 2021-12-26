package com.ifmo.lowlevel.planningcalendar.database.repository.plan.ifc

import com.ifmo.lowlevel.planningcalendar.model.Plan
import java.time.LocalDateTime

interface PlanRepository {
    fun findByDay(deadline: LocalDateTime): List<Plan>
    fun findByMonth(deadline: LocalDateTime): List<Plan>
    fun findByYear(deadline: LocalDateTime): List<Plan>
    fun findAll(): List<Plan>
    fun insert(plan: Plan)
    fun update(goal: String, plan: Plan)
    fun delete(goal: String)
}