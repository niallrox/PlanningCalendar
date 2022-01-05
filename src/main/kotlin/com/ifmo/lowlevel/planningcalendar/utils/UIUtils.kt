package com.ifmo.lowlevel.planningcalendar.utils

import com.ifmo.lowlevel.planningcalendar.model.Plan
import com.ifmo.lowlevel.planningcalendar.model.PlanState
import javafx.scene.control.Alert
import java.util.stream.Collectors
import java.util.stream.Stream

object UIUtils {

    fun viewDayPlans(plans: List<Plan>) {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        if (plans.isNotEmpty()) {
            alert.headerText = "Ваши планы на ${plans.first().getDeadline()!!.toLocalDate()}"
            alert.contentText = plansByTime(sortedPlans(plans))
        } else {
            alert.contentText = "На этот день планов нет"
        }
        alert.show()
    }

    fun sendSucceed() {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.contentText = "Успешно добавлено"
        alert.show()
    }

    fun invalidData() {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.contentText = "Введите все данные"
        alert.show()
    }

    fun userExists() {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.contentText = "Такой пользователь уже существует"
        alert.show()
    }

    fun viewPlansForPlansView(plans: List<Plan>): String {
        return if (plans.isEmpty()) {
            "Планов нет"
        } else {
            val sortedPlans = sortedPlans(plans)
            when (plans.first().getPlanState()) {
                PlanState.DAY -> plansByTime(sortedPlans)
                PlanState.MONTH -> plansByDateMonth(sortedPlans)
                PlanState.YEAR -> plansByDateYear(sortedPlans)
            }
        }
    }

    fun viewRegisterResult() {
        successfulAlert()
    }

    fun viewLoginResult(result: Boolean) {
        if (!result) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.contentText = "Неверный логин/пароль"
            alert.show()
        } else {
            successfulAlert()
        }
    }

    private fun plansByTime(sortedPlans: Stream<Plan>) =
        sortedPlans
            .map { p -> "${p.getDeadline()!!.toLocalTime()} - ${p.getGoal()}\n" }
            .collect(Collectors.toList())
            .toString()

    private fun plansByDateMonth(sortedPlans: Stream<Plan>) =
        sortedPlans
            .map { p -> "${p.getDeadline()!!.toLocalDate()} - ${p.getGoal()}\n" }
            .collect(Collectors.toList())
            .toString()

    private fun plansByDateYear(sortedPlans: Stream<Plan>) =
        sortedPlans
            .map { p -> "${p.getDeadline()!!.toLocalDate().year} - ${p.getGoal()}\n" }
            .collect(Collectors.toList())
            .toString()

    private fun sortedPlans(plans: List<Plan>) = plans.stream()
        .sorted { o1, o2 -> o1.getDeadline()!!.compareTo(o2.getDeadline()) }

    private fun successfulAlert() {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.contentText = "Успешно авторизованы"
        alert.show()
    }
}