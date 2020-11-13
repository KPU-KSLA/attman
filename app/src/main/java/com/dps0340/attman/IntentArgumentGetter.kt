package com.dps0340.attman

import android.content.Intent
import android.os.Parcelable

object IntentArgumentGetter {
    fun getStrings(intent: Intent, names: List<String>) : Map<String, String> = names.map {
        it to (intent.getStringExtra(it) ?: "")
    }.toMap()
    fun getBooleans(intent: Intent, names: List<String>) : Map<String, Boolean> = names.map {
        it to (intent.getBooleanExtra(it, false))
    }.toMap()
    fun putStrings(intent: Intent, strings: Map<String, String>) = strings.forEach {
        intent.putExtra(it.key, it.value)
    }
    fun putBooleans(intent: Intent, strings: Map<String, Boolean>) = strings.forEach {
        intent.putExtra(it.key, it.value)
    }
}