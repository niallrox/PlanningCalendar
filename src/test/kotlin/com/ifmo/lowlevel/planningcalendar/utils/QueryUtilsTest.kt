package com.ifmo.lowlevel.planningcalendar.utils

import com.ifmo.lowlevel.planningcalendar.model.Plan
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class QueryUtilsTest{

    @Test
    fun selectQueryUtilsTest(){
        val select = QueryUtils.selectWithConditions(Plan::class, "*", mapOf(Pair("goal", "goal")))
        assertEquals("SELECT * FROM plan WHERE goal=goal;", select)
    }
}