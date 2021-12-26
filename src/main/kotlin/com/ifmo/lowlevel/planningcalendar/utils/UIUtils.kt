package com.ifmo.lowlevel.planningcalendar.utils

import com.ifmo.lowlevel.planningcalendar.model.Plan
import javafx.scene.control.Alert
import tornadofx.launch

object UIUtils {

    fun viewDayPlans(plans: List<Plan>) {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.contentText = plans.toString()
        alert.show()
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

    fun viewRegisterResult(result: Boolean) {
        if (!result) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.contentText = "Пользователь с таким логином уже существует"
            alert.show()
        } else {
            successfulAlert()
        }
    }

    private fun successfulAlert() {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.contentText = "Успешно авторизованы"
        alert.show()
    }
}