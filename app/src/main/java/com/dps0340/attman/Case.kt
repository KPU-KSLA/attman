package com.dps0340.attman

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Case(
        var userID: String = "",
        var temp: Double = 0.0,
        var isDangerous: Boolean = false,
        var result: MutableMap<String, Boolean> = mutableMapOf(),
        var date: String = "",
        var qr: String = "",
        var checkedByAdmin: Boolean = false
) : Parcelable