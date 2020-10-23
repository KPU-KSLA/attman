package com.dps0340.attman

import android.graphics.drawable.ColorDrawable
import android.view.View

object ListenerFactory {
    fun makeClickListener(activity: SelfDiagnosisActivity, symptomIdx: Int, flag: Int, colorId: Int): View.OnClickListener {
        return object : View.OnClickListener {
            private var clicked = false
            private var originalColor : Int? = null
            override fun onClick(view: View) {
                val colorDrawable = view.background as ColorDrawable
                originalColor = originalColor ?: colorDrawable.color
                clicked = !clicked
                val selectedColor = if(clicked) colorId else originalColor!!
                view.setBackgroundColor(selectedColor)
                val selectedFlag = if(clicked) flag else 0
                activity.setSymptomsFlag(symptomIdx, selectedFlag)
                activity.call()
            }
        }
    }
}