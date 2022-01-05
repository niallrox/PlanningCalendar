package com.ifmo.lowlevel.planningcalendar.service.plan.impl

import com.ifmo.lowlevel.planningcalendar.database.repository.plan.ifc.PlanRepository
import com.ifmo.lowlevel.planningcalendar.ext.google.calendar.CalendarHandler
import com.ifmo.lowlevel.planningcalendar.model.Plan
import com.ifmo.lowlevel.planningcalendar.model.PlanState
import com.ifmo.lowlevel.planningcalendar.service.plan.ifc.PlanService
import java.time.LocalDate
import java.util.stream.Collectors

class PlanServiceImpl(private val googleCalendar: CalendarHandler, private val planRepository: PlanRepository) :
    PlanService {

    override fun findPlansByDay(deadline: LocalDate): List<Plan> {
        return findPlanInSetByStateDay(deadline)
    }

    override fun findPlansByMonth(deadline: LocalDate): List<Plan> {
        return findPlanInSetByStateMonth(deadline)
    }

    override fun findPlansByYear(deadline: LocalDate): List<Plan> {
        return findPlanInSetByStateYear(deadline)
    }

    override fun addPlan(plan: Plan) {
        planRepository.insert(plan)
        googleCalendar.insertEvent(plan)
    }

    override fun updatePlan(goal: String, plan: Plan) {
        planRepository.update(goal, plan)
    }

    override fun deletePlan(goal: String) {
        planRepository.delete(goal)
    }

    private fun findPlanInSetByStateDay(deadline: LocalDate) = planRepository.findAll()
        .stream()
        .filter { p -> p.getDeadline()!!.year == deadline.year }
        .filter { p -> p.getDeadline()!!.dayOfYear == deadline.dayOfYear }
        .filter { p -> p.getPlanState() == PlanState.DAY}
        .collect(Collectors.toList())

    private fun findPlanInSetByStateMonth(deadline: LocalDate) = planRepository.findAll()
        .stream()
        .filter { p -> p.getDeadline()!!.year == deadline.year }
        .filter { p -> p.getDeadline()!!.month == deadline.month }
        .filter { p -> p.getPlanState() == PlanState.MONTH }
        .collect(Collectors.toList())

    private fun findPlanInSetByStateYear(deadline: LocalDate) = planRepository.findAll()
        .stream()
        .filter { p -> p.getDeadline()!!.year == deadline.year }
        .filter { p -> p.getPlanState() == PlanState.YEAR }
        .collect(Collectors.toList())
}
