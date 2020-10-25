package com.dps0340.attman

import android.graphics.drawable.ColorDrawable
import android.view.View

object ListenerFactory {
    fun makeClickListener(activity: SelfDiagnosisActivity, symptomIdx: Int, flag: Boolean, colorId: Int, mutualView: View?, mutualListener: ButtonListener? = null): ButtonListener {
        return ButtonListener(activity, symptomIdx, flag, colorId, mutualView, mutualListener)
    }
}