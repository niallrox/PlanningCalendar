package com.ifmo.lowlevel.planningcalendar.ui

import com.ifmo.lowlevel.planningcalendar.ApplicationEnvironment
import com.ifmo.lowlevel.planningcalendar.service.plan.ifc.PlanService
import com.ifmo.lowlevel.planningcalendar.utils.UIUtils
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.ChoiceBox
import javafx.scene.control.DatePicker
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.AnchorPane
import tornadofx.View
import java.time.LocalDate

class PlansView: View() {
    override val root: AnchorPane by fxml(ApplicationEnvironment.getPropertiesByName("fxml").getProperty("plans"))
    private val dayPane: ScrollPane by fxid("dayPane")
    private val monthPane: ScrollPane by fxid("monthPane")
    private val yearPane: ScrollPane by fxid("yearPane")
    private val datePicker: DatePicker by fxid("picker")
    private val planService: PlanService = ApplicationEnvironment.planService()

    fun calendar(){
        replaceWith<CalendarView>()
    }

    fun menu(){
        replaceWith<MenuView>()
    }

    init {
        datePicker.value = LocalDate.now()
        refreshPanes()
        datePicker.onAction = dateEventHandler()
    }

    private fun dateEventHandler(): EventHandler<ActionEvent> {
        return EventHandler<ActionEvent> { refreshPanes()}
    }

    private fun refreshPanes() {
        dayPane.content = Label(UIUtils.viewPlansForPlansView(planService.findPlansByDay(datePicker.value)))
        monthPane.content = Label(UIUtils.viewPlansForPlansView(planService.findPlansByMonth(datePicker.value)))
        yearPane.content = Label(UIUtils.viewPlansForPlansView(planService.findPlansByYear(datePicker.value)))
    }
}