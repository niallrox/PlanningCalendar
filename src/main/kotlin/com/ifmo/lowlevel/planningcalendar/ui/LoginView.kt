package com.ifmo.lowlevel.planningcalendar.ui

import com.ifmo.lowlevel.planningcalendar.ApplicationEnvironment
import com.ifmo.lowlevel.planningcalendar.model.handler.SecurityHandler
import com.ifmo.lowlevel.planningcalendar.utils.UIUtils
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import mu.KotlinLogging
import tornadofx.View
import java.lang.IllegalArgumentException
import java.sql.SQLException

class LoginView : View() {
    override val root: AnchorPane by fxml(ApplicationEnvironment.getPropertiesByName("fxml").getProperty("login"))
    private val loginField: TextField by fxid("loginInput")
    private val logger = KotlinLogging.logger {}
    private val passwordField: PasswordField by fxid("passwordInput")

    private val userService = ApplicationEnvironment.userService()


    fun login() {
        try {
            val result = userService.login(loginField.text, passwordField.text)
            UIUtils.viewLoginResult(result)
            if (result) {
                initMainMenu()
            }
        } catch (e: SQLException) {
            logger.error { "Error with log in executing $e" }
        } catch (e: IllegalArgumentException) {
            UIUtils.invalidData()
        }
    }

    fun register() {
        try {
            val result = userService.register(loginField.text, passwordField.text)
            UIUtils.viewRegisterResult()
            if (result) {
                initMainMenu()
            }
        } catch (e: SQLException) {
            UIUtils.userExists()
        } catch (e: IllegalArgumentException) {
            UIUtils.invalidData()
        }
    }

    private fun initMainMenu() {
        SecurityHandler.setUser(loginField.text)
        replaceWith<CalendarView>()
    }
}