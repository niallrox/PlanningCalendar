package com.ifmo.lowlevel.planningcalendar.ui

import com.ifmo.lowlevel.planningcalendar.ApplicationEnvironment
import com.ifmo.lowlevel.planningcalendar.utils.UIUtils
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import tornadofx.View

class LoginView : View() {
    override val root: AnchorPane by fxml(ApplicationEnvironment.getPropertiesByName("fxml").getProperty("login"))
    private val loginField: TextField by fxid("loginInput")
    private val passwordField: PasswordField by fxid("passwordInput")

    private val userService = ApplicationEnvironment.userService()


    fun login() {
        val result = userService.login(loginField.text, passwordField.text)
        UIUtils.viewLoginResult(result)
        if (result) {
            replaceWith<CalendarView>()
        }
    }

    fun register() {
        val result = userService.register(loginField.text, passwordField.text)
        UIUtils.viewRegisterResult(result)
        if (result) {
            replaceWith<CalendarView>()
        }
    }

}