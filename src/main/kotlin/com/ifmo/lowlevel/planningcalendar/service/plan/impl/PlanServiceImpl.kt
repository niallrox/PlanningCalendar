package com.ifmo.lowlevel.planningcalendar.service.plan.impl

import com.ifmo.lowlevel.planningcalendar.database.repository.plan.ifc.PlanRepository
import com.ifmo.lowlevel.planningcalendar.model.Plan
import com.ifmo.lowlevel.planningcalendar.model.PlanState
import com.ifmo.lowlevel.planningcalendar.model.handler.PlansHandler
import com.ifmo.lowlevel.planningcalendar.service.plan.ifc.PlanService
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.stream.Collectors

class PlanServiceImpl(private val plansHandler: PlansHandler, private val planRepository: PlanRepository) :
    PlanService {

    override fun findPlansByDay(deadline: LocalDate): List<Plan> {
        return findPlanInSetByState(deadline, PlanState.DAY)
    }

    override fun findPlansByMonth(deadline: LocalDate): List<Plan> {
        return findPlanInSetByState(deadline, PlanState.MONTH)
    }

    override fun findPlansByYear(deadline: LocalDate): List<Plan> {
        return findPlanInSetByState(deadline, PlanState.YEAR)
    }

    override fun addPlan(plan: Plan) {
        planRepository.insert(plan)
        plansHandler.addToHandler(plan)
    }

    override fun updatePlan(goal: String, plan: Plan) {
        planRepository.update(goal, plan)
        plansHandler.update(plan)
    }

    override fun deletePlan(goal: String) {
        planRepository.delete(goal)
        plansHandler.removeFromHandler(goal)
    }

    private fun findPlanInSetByState(deadline: LocalDate, planState: PlanState) = plansHandler.getElements()
        .stream()
        .filter { p -> p.getDeadline()!!.toInstant(ZoneOffset.UTC).toEpochMilli() >=
                deadline.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli() }
        .filter { p -> p.getPlanState() == planState }
        .collect(Collectors.toList())
}
