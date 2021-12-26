package com.ifmo.lowlevel.planningcalendar.utils

import java.util.stream.Collectors
import kotlin.reflect.KClass

object QueryUtils {

    fun <E : Any> insert(clazz: KClass<E>): String {
        return "INSERT INTO ${clazz.simpleName!!.lowercase()} VALUES (${getQuestsFromClass(clazz)});"
    }

    fun <E : Any> update(clazz: KClass<E>, conditionField: String): String {
        return "UPDATE ${clazz.simpleName!!.lowercase()} SET ${getFieldsForUpdate(clazz)} WHERE $conditionField = ?;"
    }

    fun <E : Any> selectWithConditions(clazz: KClass<E>, fieldsToFind: String, conditions: Map<String, String>): String {
        return "SELECT $fieldsToFind FROM ${clazz.simpleName!!.lowercase()} WHERE ${conditionFromMap(conditions)};"
    }

    fun <E : Any> select(clazz: KClass<E>, fieldsToFind: String): String {
        return "SELECT $fieldsToFind FROM ${clazz.simpleName!!.lowercase()};"
    }

    fun <E : Any> exists(clazz: KClass<E>, conditions: Map<String, String>): String {
        return "SELECT EXISTS " +
                "(SELECT * FROM ${clazz.simpleName!!.lowercase()} WHERE ${conditionFromMap(conditions)}) AS rows;"
    }

    fun <E : Any> delete(clazz: KClass<E>, conditions: Map<String, String>): String {
        return "DELETE FROM ${clazz.simpleName!!.lowercase()} WHERE ${conditionFromMap(conditions)};"
    }

    private fun <E : Any> getFieldsForUpdate(clazz: KClass<E>) : String {
        val fieldsName = Class.forName(clazz.qualifiedName).declaredFields.map { field -> field.name }
        return fieldsName.joinToString(separator = "=?, ")
    }

    private fun conditionFromMap(conditions: Map<String, String>): String {
        val conditionsList = conditions.entries.stream().map { (k, v) -> "$k=${v}" }.collect(Collectors.toList())
        return conditionsList.joinToString(separator = " AND ")
    }

    private fun <E : Any> getQuestsFromClass(clazz: KClass<E>): String {
        val size = Class.forName(clazz.qualifiedName).declaredFields.size
        val quests = StringBuilder()
        for (i in 1 until size) {
            quests.append("?, ")
        }
        return quests.toString()
    }
}