package com.dps0340.attman

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(MyView(this))
        //setContentView(R.layout.activity_main);
    }

    override fun onTouchEvent(event: MotionEvent): Boolean { //터치시 다음화면인 로그인화면으로 이동
        val action = event.action
        if (action == MotionEvent.ACTION_DOWN) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        return super.onTouchEvent(event)
    }
}