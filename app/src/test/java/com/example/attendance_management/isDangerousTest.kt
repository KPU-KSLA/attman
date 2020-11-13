package com.example.attendance_management

import android.content.Intent
import com.dps0340.attman.SYMPTOMS
import com.dps0340.attman.ScanActivityCaller
import com.dps0340.attman.SelfDiagnosisActivity
import org.junit.Assert.*
import org.junit.Test

class isDangerousTest {
    val defaultActivity = SelfDiagnosisActivity()
    val defaultIntent = Intent(null, S)
    val defaultVisited = SYMPTOMS.englishFull.indices.map { _ -> true}
    lateinit var flags: Map<String, Boolean>

    fun generateTestCaller(activity: SelfDiagnosisActivity = defaultActivity, intent: Intent = defaultIntent, visited: List<Boolean> = defaultVisited): ScanActivityCaller {
        activity.constructFlagMap()
        flags = activity.flagMap
        val scanActivityCaller = ScanActivityCaller(visited as MutableList<Boolean>, intent, flags as MutableMap<String, Boolean>)
        return scanActivityCaller
    }

    @Test
    fun isDangerous_ReturnsTrue() {
        val trueCaller = generateTestCaller()
        assertTrue("scanActivityCaller.isDangerous() with true map always return true.", trueCaller.isDangerous())
    }

    @Test
    fun isDangerous_ReturnsFalse() {
        val mixedCaller = generateTestCaller(visited = listOf(true, true, false, true, false))
        assertFalse("scanActivityCaller.isDangerous() with mixed Boolean map always return false.", mixedCaller.isDangerous())
        val falseCaller = generateTestCaller(visited = listOf(false, false, false, false, false))
        assertFalse("scanActivityCaller.isDangerous() with mixed false map always return false.", falseCaller.isDangerous())
    }
}