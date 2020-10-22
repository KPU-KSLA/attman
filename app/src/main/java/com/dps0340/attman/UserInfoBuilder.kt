package com.dps0340.attman

class UserInfoBuilder {
    fun build(id: String, rawPassword: String, name: String, stdNum: String, email: String) : UserInfo {
        val encryptedPassword = sha512.encrypt(rawPassword)
        return UserInfo(id, encryptedPassword, name, stdNum, email)
    }
}