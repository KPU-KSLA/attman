package com.dps0340.attman

import android.graphics.drawable.ColorDrawable
import android.view.View

class ButtonListener(private val activity: SelfDiagnosisActivity, val symptomIdx: Int, private val flag: Boolean, val colorId: Int, val mutualView: View?, var mutualListener: ButtonListener?): View.OnClickListener {
    private var clicked = false
        private set
    private var originalColor : Int? = null
    override fun onClick(view: View) {
        val colorDrawable = view.background as ColorDrawable
        originalColor = originalColor ?: colorDrawable.color
        clicked = !clicked
        val selectedColor = if(clicked) colorId else originalColor!!
        view.setBackgroundColor(selectedColor)
        if(clicked) {
            mutualView?.let { view ->
                mutualListener?.let { listener ->
                    if(listener.clicked) {
                        view.performClick()
                    }
                }
            }
            activity.setSymptomsFlag(symptomIdx, flag)
            activity.call()
        }
    }
}