package com.ifmo.lowlevel.planningcalendar.model.handler

import com.ifmo.lowlevel.planningcalendar.model.Plan
import java.util.stream.Collectors

class PlansHandler(private val set : HashSet<Plan>) {

    fun getElements(): Set<Plan> {
        return set.toMutableSet()
    }

    fun addToHandler(plan: Plan) {
        set.add(plan)
    }

    fun addAllToHandler(plan: Plan) {
        set.add(plan)
    }

    fun removeFromHandler(goal: String) {
        set.removeIf { p -> p.getGoal() == goal }
    }

    fun update(plan: Plan) {
        set.removeIf { p -> p.getGoal() == plan.getGoal() }
        set.add(plan)
    }
}