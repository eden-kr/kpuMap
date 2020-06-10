package com.example.kpumap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class startActivity : AppCompatActivity() {
    //로딩화면 구현
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val handler = Handler()
        handler.postDelayed(Runnable {
            run {
                val intent = Intent(baseContext,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)
    }
}
