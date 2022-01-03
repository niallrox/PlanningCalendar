package com.ifmo.lowlevel.planningcalendar.ui

import com.ifmo.lowlevel.planningcalendar.ApplicationEnvironment
import javafx.scene.control.DatePicker
import javafx.scene.control.ScrollPane
import javafx.scene.layout.AnchorPane
import tornadofx.View

class PlansView: View() {
    override val root: AnchorPane by fxml(ApplicationEnvironment.getPropertiesByName("fxml").getProperty("plans"))
    private val dayPane: ScrollPane by fxid("dayPane")
    private val monthPane: ScrollPane by fxid("monthPane")
    private val yearPane: ScrollPane by fxid("yearPane")
    private val datePicker: DatePicker by fxid("picker")

    fun calendar(){
        replaceWith<CalendarView>()
    }
}