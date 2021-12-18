package com.ifmo.lowlevel.planningcalendar.utils

import kotlin.reflect.KClass

object QueryUtils {

    fun insert(clazz: KClass<Any>): String {
        return "INSERT INTO  ${clazz.simpleName!!.lowercase()} VALUES (${getQuestsFromClass(clazz)})"
    }

    private fun getQuestsFromClass(clazz: KClass<Any>): String {
        val size = Class.forName(clazz.qualifiedName).declaredFields.size
        val quests = StringBuilder()
        for (i in 1 until size) {
            quests.append("?, ")
        }
        return quests.toString()
    }
}