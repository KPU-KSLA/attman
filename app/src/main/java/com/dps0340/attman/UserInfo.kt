package com.dps0340.attman

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class UserInfo(
        var id: String = "",
        var password: String = "",
        var name: String = "",
        var stdNum: String = "",
        var email: String = ""
) : Parcelable