package com.ifmo.lowlevel.planningcalendar.model.handler

import com.ifmo.lowlevel.planningcalendar.model.Plan

class PlansHandler(private val set: HashSet<Plan>) {
    fun addToSet(plan: Plan){
        set.add(plan)
    }
}