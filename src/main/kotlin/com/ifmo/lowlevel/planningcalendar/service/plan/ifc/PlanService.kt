package com.ifmo.lowlevel.planningcalendar.service.plan.ifc

import com.ifmo.lowlevel.planningcalendar.model.Plan
import java.time.LocalDate

interface PlanService {
    fun findPlansByDay(deadline: LocalDate): List<Plan>
    fun findPlansByMonth(deadline: LocalDate): List<Plan>
    fun findPlansByYear(deadline: LocalDate): List<Plan>
    fun addPlan(plan: Plan)
    fun updatePlan(goal: String, plan: Plan)
    fun deletePlan(goal: String)
}