package com.example.attendance_management

import android.content.Intent
import com.dps0340.attman.SYMPTOMS
import com.dps0340.attman.ScanActivityCaller
import com.dps0340.attman.ScanActivityCaller.Companion.isDangerous
import com.dps0340.attman.SelfDiagnosisActivity
import org.junit.Assert.*
import org.junit.Test

class isDangerousTest {
    @Test
    fun isDangerous_ReturnsTrue() {
        val trueList = listOf(true, true, true, true, true)
        val trueMap = listOf(trueList.indices).map { it.toString() }.zip(trueList).toMap()
        val mixList = listOf(true, true, false, false, false)
        val mixedMap = listOf(mixList.indices).map { it.toString() }.zip(mixList).toMap()
        assertTrue("scanActivityCaller.isDangerous() with true map always return true.", isDangerous(trueMap))
        assertTrue("scanActivityCaller.isDangerous() with mixed Boolean map always return true.", isDangerous(mixedMap))
    }

    @Test
    fun isDangerous_ReturnsFalse() {
        val falseList = listOf(false, false, false, false, false)
        val falseMap = listOf(falseList.indices).map{ it.toString() }.zip(falseList).toMap()
        assertFalse("scanActivityCaller.isDangerous() with mixed false map always return false.", isDangerous(falseMap))
    }
}