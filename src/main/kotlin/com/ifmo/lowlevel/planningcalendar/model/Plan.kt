package com.ifmo.lowlevel.planningcalendar.model

import java.time.LocalDateTime

class Plan private constructor(
    private val goal: String, private val completed: Boolean, private val description: String,
    private val deadline: LocalDateTime?, private val planState: PlanState
) {

    fun getGoal() = goal
    fun getDescription() = description
    fun getDeadline() = deadline
    fun isCompleted() = completed
    fun getPlanState() = planState

    companion object PlanBuilder {
        private var goal = ""
        private var completed = false
        private var description = ""
        private var deadline: LocalDateTime? = null
        private var planState = PlanState.DAY

        fun goal(goal: String) {
            this.goal = goal
        }

        fun completed(completed: Boolean) {
            this.completed = completed
        }

        fun description(description: String) {
            this.description = description
        }

        fun deadline(deadline: LocalDateTime?) {
            this.deadline = deadline
        }

        fun planState(planState: PlanState) {
            this.planState = planState
        }

        fun build(){
            Plan(goal, completed, description, deadline, planState)
        }
    }


    override fun toString(): String {
        return goal
    }

}