package com.dps0340.attman

import android.graphics.drawable.ColorDrawable
import android.view.View

class ButtonListener(activity: SelfDiagnosisActivity, symptomIdx: Int, flag: Boolean, colorId: Int, mutualView: View?, mutualListener: ButtonListener?): View.OnClickListener {
    val mutualView: View? = mutualView
    var mutualListener: ButtonListener? = mutualListener
    var clicked = false
        private set
    val activity: SelfDiagnosisActivity = activity
    val colorId = colorId
    val symptomIdx = symptomIdx
    val flag = flag
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