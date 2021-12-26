package com.ifmo.lowlevel.planningcalendar.ui

import com.ifmo.lowlevel.planningcalendar.ApplicationEnvironment
import com.ifmo.lowlevel.planningcalendar.model.common.SysConsts
import com.ifmo.lowlevel.planningcalendar.utils.UIUtils
import com.sun.javafx.scene.control.LabeledText
import javafx.event.*
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import tornadofx.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarView : View() {
    override val root: AnchorPane by fxml(ApplicationEnvironment.getPropertiesByName("fxml").getProperty("calendar"))
    private val button1: Button by fxid("button1")
    private val button2: Button by fxid("button2")
    private val button3: Button by fxid("button3")
    private val button4: Button by fxid("button4")
    private val button5: Button by fxid("button5")
    private val button6: Button by fxid("button6")
    private val button7: Button by fxid("button7")
    private val button8: Button by fxid("button8")
    private val button9: Button by fxid("button9")
    private val button10: Button by fxid("button10")
    private val button11: Button by fxid("button11")
    private val button12: Button by fxid("button12")
    private val button13: Button by fxid("button13")
    private val button14: Button by fxid("button14")
    private val button15: Button by fxid("button15")
    private val button16: Button by fxid("button16")
    private val button17: Button by fxid("button17")
    private val button18: Button by fxid("button18")
    private val button19: Button by fxid("button19")
    private val button20: Button by fxid("button20")
    private val button21: Button by fxid("button21")
    private val button22: Button by fxid("button22")
    private val button23: Button by fxid("button23")
    private val button24: Button by fxid("button24")
    private val button25: Button by fxid("button25")
    private val button26: Button by fxid("button26")
    private val button27: Button by fxid("button27")
    private val button28: Button by fxid("button28")
    private val button29: Button by fxid("button29")
    private val button30: Button by fxid("button30")
    private val button31: Button by fxid("button31")
    private val button32: Button by fxid("button32")
    private val button33: Button by fxid("button33")
    private val button34: Button by fxid("button34")
    private val button35: Button by fxid("button35")
    private val button36: Button by fxid("button36")
    private val button37: Button by fxid("button37")
    private val button38: Button by fxid("button38")
    private val button39: Button by fxid("button39")
    private val button40: Button by fxid("button40")
    private val button41: Button by fxid("button41")
    private val button42: Button by fxid("button42")
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

    private fun buttonEventHandler(): EventHandler<ActionEvent> {
        return EventHandler<ActionEvent> { event ->
            val bEvent = event.target as Button
            val date = LocalDate.of(localDate.year, localDate.month, bEvent.text.toInt())
            UIUtils.viewDayPlans(planService.findPlansByDay(date))
        }
    }

    private fun listOfButton() = listOf(
        button1, button2, button3, button4, button5, button6, button7, button8,
        button9, button10, button11, button12, button13, button14, button15, button16, button17,
        button18, button19, button20, button21, button22, button23, button24, button25,
        button26, button27, button28, button29, button30, button31, button32, button33, button34, button35,
        button36, button37, button38, button39, button40, button41, button42
    )
}
