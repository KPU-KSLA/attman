package com.dps0340.attman

import android.app.Activity
import android.content.Intent
import com.google.gson.Gson


class ScanActivityCaller(visited: MutableList<Boolean>,
                         currentIntent: Intent,
                         flags: MutableMap<String, Boolean>): Caller(visited, currentIntent, flags) {
    override fun call(activity: Activity) {
        if(visited.any{ e -> !e }) {
            return
        }
        val destIntent = Intent(activity.baseContext, ScanActivity::class.java)
        val arguments = IntentArgumentGetter.getStrings(currentIntent, INTENT_ARGUMENTS.strings)
        IntentArgumentGetter.putStrings(destIntent, arguments)
        flags.forEach {
            destIntent.putExtra(it.key, it.value)
        }
        val isDangerous = flags.any { (_, v) -> v }
        destIntent.putExtra("dangerous?", isDangerous)
        val gson = Gson()
        destIntent.putExtra("result", gson.toJson(flags))
        activity.startActivity(destIntent)
    }
}