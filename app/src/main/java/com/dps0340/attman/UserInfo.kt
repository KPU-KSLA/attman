package com.dps0340.attman

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserInfo(
        var id: String,
        var password: String,
        var name: String,
        var stdNum: String,
        var email: String
)