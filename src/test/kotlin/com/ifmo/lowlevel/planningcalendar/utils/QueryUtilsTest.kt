package com.ifmo.lowlevel.planningcalendar.utils

import com.ifmo.lowlevel.planningcalendar.model.Plan
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class QueryUtilsTest{

    @Test
    fun conditionSeparatorTest(){
        val listOf = listOf("a", "b", "c")
        assertEquals("a AND b AND c", listOf.joinToString(separator = " AND ") )
    }

    @Test
    fun selectGenericTest(){
        val select = QueryUtils.select(Plan::class, "*", mapOf(Pair("goal", "goal")))
        assertEquals("SELECT * FROM plan WHERE goal=goal;", select)
    }
}