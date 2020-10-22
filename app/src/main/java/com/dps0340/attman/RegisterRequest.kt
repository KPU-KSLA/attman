package com.dps0340.attman

import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import java.util.*

class RegisterRequest(userID: String, userPassword: String, userName: String, userNumber: Int, userEmail: String, listener: Response.Listener<String?>?) : StringRequest(Method.POST, URL, listener, null) {
    private val map: MutableMap<String, String>
    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String> {
        return map
    }

    companion object {
        private const val URL = "http://an950120.dothome.co.kr/Register.php"
    }

    init {
        map = HashMap()
        map["userID"] = userID
        map["userPassword"] = userPassword
        map["userName"] = userName
        map["userNumber"] = "$userNumber "
        map["userEmail"] = userEmail
    }
}