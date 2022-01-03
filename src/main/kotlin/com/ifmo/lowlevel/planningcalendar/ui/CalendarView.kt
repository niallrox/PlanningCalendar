package com.ifmo.lowlevel.planningcalendar.ui

import com.ifmo.lowlevel.planningcalendar.ApplicationEnvironment
import com.ifmo.lowlevel.planningcalendar.model.common.SysConsts
import com.ifmo.lowlevel.planningcalendar.utils.UIUtils
import com.sun.javafx.scene.control.LabeledText
import javafx.event.*
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import tornadofx.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

class CalendarView : View() {
    override val root: AnchorPane by fxml(ApplicationEnvironment.getPropertiesByName("fxml").getProperty("calendar"))
    private val buttonBar: ButtonBar by fxid("buttonBar")
    private val dateLabel: Label by fxid("dateLabel")

    private var localDate = LocalDate.now().withDayOfMonth(1)

    private var buttonList: List<Button> = listOfButton()

    private val planService = ApplicationEnvironment.planService()

    init {
        getCalendar()
        buttonList
            .stream()
            .forEach { button -> button.onAction = buttonEventHandler() }
    }

    private fun getCalendar() {
        var i = 1
        clearButtonList()
        buttonList.stream()
            .skip(localDate.dayOfWeek.value.toLong() - 1)
            .limit(localDate.lengthOfMonth().toLong())
            .forEach { button -> button.text = i++.toString() }
        dateLabel.text = DateTimeFormatter.ofPattern(SysConsts.DATE_FORMAT).format(localDate)
    }

    private fun clearButtonList() {
        buttonList.forEach { button -> button.text = "" }
    }

    fun left() {
        localDate = localDate.minusMonths(1)
        getCalendar()
    }

    fun right() {
        localDate = localDate.plusMonths(1)
        getCalendar()
    }

    fun plans() {
        replaceWith<PlansView>()
    }

    private fun buttonEventHandler(): EventHandler<ActionEvent> {
        return EventHandler<ActionEvent> { event ->
            val bEvent = event.target as Button
            if (bEvent.text.isNotEmpty()) {
                val date = LocalDate.of(localDate.year, localDate.month, bEvent.text.toInt())
                UIUtils.viewDayPlans(planService.findPlansByDay(date))
            }
        }
    }

    private fun listOfButton() = (buttonBar.buttons.first() as VBox).children.stream()
            .limit(7)
            .map { node -> node as HBox }
            .map { node -> node.children }
            .flatMap { buttons -> buttons.stream() }
            .filter { node -> node is Button }
            .map { node -> node as Button  }
            .collect(Collectors.toList())
}
