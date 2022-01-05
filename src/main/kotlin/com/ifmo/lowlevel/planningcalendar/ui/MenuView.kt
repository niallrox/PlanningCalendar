package com.ifmo.lowlevel.planningcalendar.ui

import com.ifmo.lowlevel.planningcalendar.ApplicationEnvironment
import com.ifmo.lowlevel.planningcalendar.model.Plan
import com.ifmo.lowlevel.planningcalendar.model.PlanState
import com.ifmo.lowlevel.planningcalendar.model.handler.SecurityHandler
import com.ifmo.lowlevel.planningcalendar.utils.UIUtils
import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.DatePicker
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import se.alipsa.ymp.YearMonthPicker
import se.alipsa.ymp.YearMonthPickerCombo
import tornadofx.View
import tornadofx.control.DateTimePicker
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

class MenuView : View() {
    override val root: AnchorPane by fxml(ApplicationEnvironment.getPropertiesByName("fxml").getProperty("menu"))
    private val goal: TextField by fxid("goal")
    private val description: TextField by fxid("description")
    private val datePicker: DateTimePicker by fxid("datePicker")
    private val yearMonthPicker: YearMonthPickerCombo by fxid("yearMonthPicker")
    private val yearPicker: ChoiceBox<Int> by fxid("yearPicker")
    private val typePicker: ChoiceBox<PlanState> by fxid("typePicker")
    private val send: Button by fxid("send")
    private val planService = ApplicationEnvironment.planService()


    init {
        init()
    }

    fun calendar() {
        replaceWith<CalendarView>()
    }

    fun plans() {
        replaceWith<PlansView>()
    }

    fun send() {
        planService.addPlan(buildPlan())
        UIUtils.sendSucceed()
    }

    private fun buildPlan() = Plan
        .goal(goal.text)
        .description(description.text)
        .planState(typePicker.value)
        .deadline(evaluateDeadline())
        .userLogin(SecurityHandler.currentUser().get())
        .build()

    private fun evaluateDeadline(): LocalDateTime {
        return when (typePicker.value) {
            PlanState.YEAR -> {
                LocalDate.of(yearPicker.value, 12, 1)
                    .atStartOfDay().with(TemporalAdjusters.lastDayOfMonth())
            }
            PlanState.MONTH -> {
                LocalDate.of(yearMonthPicker.value.year, yearMonthPicker.value.month, 1)
                    .atStartOfDay().with(TemporalAdjusters.lastDayOfMonth())
            }
            PlanState.DAY -> {
                datePicker.dateTimeValue
            }
        }
    }

    private fun init() {
        initSendButton()
        initYearPicker()
        initTypePicker()
    }

    private fun initSendButton() {
        send.disableProperty().bind(
            Bindings.isEmpty(goal.textProperty())
                .and(Bindings.isEmpty(description.textProperty()))
                .and(Bindings.isNull(typePicker.valueProperty()))
        )
    }

    private fun initTypePicker() {
        typePicker.items = Arrays
            .stream(PlanState.values())
            .collect(Collectors.toCollection(FXCollections::observableArrayList))
        typePicker.onAction = boxEventHandler()
        typePicker.value = PlanState.DAY
    }

    private fun initYearPicker() {
        val startDate = 1950
        yearPicker.items = Stream.iterate(startDate, { x -> x + 1 })
            .limit((LocalDate.now().year + 50 - startDate).toLong())
            .collect(Collectors.toCollection(FXCollections::observableArrayList))
    }

    private fun boxEventHandler(): EventHandler<ActionEvent> {
        return EventHandler<ActionEvent> { event ->
            val bEvent = event.target as ChoiceBox<*>
            hideAnother(bEvent)
        }
    }

    private fun hideAnother(bEvent: ChoiceBox<*>) {
        when (bEvent.value) {
            PlanState.YEAR -> {
                yearMonthPicker.isVisible = false
                datePicker.isVisible = false
                yearPicker.isVisible = true
            }
            PlanState.MONTH -> {
                yearPicker.isVisible = false
                datePicker.isVisible = false
                yearMonthPicker.isVisible = true
            }
            PlanState.DAY -> {
                yearMonthPicker.isVisible = false
                yearPicker.isVisible = false
                datePicker.isVisible = true
            }
        }
    }
}
