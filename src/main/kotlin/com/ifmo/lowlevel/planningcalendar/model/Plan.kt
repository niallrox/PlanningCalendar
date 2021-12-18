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

    companion object Builder {
        private var goal = ""
        private var completed = false
        private var description = ""
        private var deadline: LocalDateTime? = null
        private var planState = PlanState.DAY

        fun goal(goal: String) : Builder{
            this.goal = goal
            return this
        }

        fun completed(completed: Boolean) : Builder {
            this.completed = completed
            return this
        }

        fun description(description: String): Builder {
            this.description = description
            return this
        }

        fun deadline(deadline: LocalDateTime?): Builder {
            this.deadline = deadline
            return this
        }

        fun planState(planState: PlanState): Builder {
            this.planState = planState
            return this
        }

        fun build() : Plan{
            return Plan(goal, completed, description, deadline, planState)
        }
    }


    override fun toString(): String {
        return goal
    }

    override fun hashCode(): Int {
        var result = 12 * goal.hashCode()
        result += 13 * completed.hashCode()
        result += 21 * deadline.hashCode()
        result += 37 * description.hashCode()
        result += 38 * planState.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Plan) return false
        return other.goal == goal && other.completed == completed
                && other.description==description && other.planState == planState
    }

}