package com.ifmo.lowlevel.planningcalendar

import mu.KotlinLogging

fun main() {
    val logger = KotlinLogging.logger {}
    logger.debug { "Application starting" }
    Application.run()
    logger.debug { "Application finished" }
}