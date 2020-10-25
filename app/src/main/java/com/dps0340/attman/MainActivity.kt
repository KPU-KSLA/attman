package com.dps0340.attman

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(MyView(this))
        Handler(Looper.getMainLooper()).postDelayed({
            this.onClick(null)
            Log.i("MAINACTIVITY", "set onclick after 2 sec")
        }, 2000)
    }

    override fun onClick(view: View?) {
        Log.i("MAINACTIVITY", "start LoginActivity")
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}