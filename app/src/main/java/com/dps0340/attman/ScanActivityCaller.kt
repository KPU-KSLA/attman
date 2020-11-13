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
        val arguments = IntentArgumentHandler.getStrings(currentIntent, INTENT_ARGUMENTS.strings)
        IntentArgumentHandler.putStrings(destIntent, arguments)
        flags.forEach {
            destIntent.putExtra(it.key, it.value)
        }
        val isDangerous = isDangerous(flags)
        destIntent.putExtra("dangerous?", isDangerous)
        val gson = Gson()
        destIntent.putExtra("result", gson.toJson(flags))
        activity.startActivity(destIntent)
    }

    companion object {
        /**
         * if any key is true, returns true.
         * true means the person is dangerous condition.
         */
        fun isDangerous(flags: Map<String, Boolean>): Boolean = flags.any { (_, v) -> v}
    }
}