package com.dps0340.attman

import android.app.Activity
import android.content.Intent

/**
 * an Abstract class which using SelfDiagnosisActivity
 * Strategy pattern of Call method
 */
abstract class Caller(
        val visited: MutableList<Boolean>,
        val currentIntent: Intent,
        val flags: MutableMap<String, Boolean>
) {
    abstract fun call(activity: Activity)
}