package com.dps0340.attman

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Case(
    var userID: String = "",
    var temp: Double = 0.0,
    var imgUri: String = "",
    var isDangerous: Boolean = false,
    var result: List<Int>,
    var qr: String = ""
) : Parcelable

fun Case.convertToMap(): MutableMap<String, Any> {
    val map = mutableMapOf<String, Any>()
    map["userID"] = userID
    map["imgUri"] = imgUri
    map["isDangerous"] = isDangerous
    map["result"] = result
    map["qr"] = qr
    return map
}