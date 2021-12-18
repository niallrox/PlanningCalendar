package com.ifmo.lowlevel.planningcalendar

import java.io.FileInputStream
import java.util.*

object Application {

    private val properties = Properties()

    init {
        val fileInputStream = FileInputStream("src/main/resources/url-handler.properties")
        properties.load(fileInputStream)
    }

    fun getUrlByPropertyHandler(string: String): Properties {
        val url = properties.getProperty(string)
        val fileInputStream = FileInputStream(url)
        val propertiesByKey = Properties()
        propertiesByKey.load(fileInputStream)
        return propertiesByKey
    }

    fun run(){
        TODO()
    }
}