package com.ifmo.lowlevel.planningcalendar.database.repository.ifc

import com.ifmo.lowlevel.planningcalendar.model.Plan

interface PlanRepository {
    fun findByDay(goal: String): List<Plan>
    fun findByMonth(goal: String): List<Plan>
    fun findByYear(goal: String): List<Plan>
    fun insert(plan: Plan)
    fun update(goal: String, plan: Plan)
    fun delete(goal: String)
}