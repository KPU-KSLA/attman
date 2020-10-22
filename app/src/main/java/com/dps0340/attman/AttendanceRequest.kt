package com.dps0340.attman

import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import java.util.*

class AttendanceRequest(attendanceInformation: String, time: String, listener: Response.Listener<String?>?) : StringRequest(Method.POST, URL, listener, null) {
    private val map: MutableMap<String, String>
    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String> {
        return map
    }

    companion object {
        //서버 URL 설정(php 파일 연동)
        private const val URL = "http://an950120.dothome.co.kr/Attendance2.php"
    }

    //private Map<String, String>parameters;
    init {
        map = HashMap()
        map["attendanceInformation"] = attendanceInformation
        map["time"] = time
    }
}