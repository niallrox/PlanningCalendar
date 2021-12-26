package com.ifmo.lowlevel.planningcalendar

import com.ifmo.lowlevel.planningcalendar.ui.UI
import mu.KotlinLogging
import tornadofx.launch

fun main() {
    val logger = KotlinLogging.logger {}
    logger.debug { "Application starting" }
    launch<UI>()
    logger.debug { "Application finished" }
}