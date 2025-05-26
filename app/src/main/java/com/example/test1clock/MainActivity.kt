package com.example.test1clock

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.test1clock.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("Asia/Shanghai")
    }
    private val updateTimeTask = object : Runnable {
        override fun run() {
            val currentTime = timeFormat.format(Date())
            binding.clockText.text = currentTime
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔍 这行必须是 binding.root，不能是 R.layout.activity_main
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler.post(updateTimeTask)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimeTask)
    }
}
