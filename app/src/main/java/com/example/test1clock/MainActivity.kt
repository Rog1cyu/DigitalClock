package com.example.test1clock

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var hourTens: TextSwitcher
    private lateinit var hourUnits: TextSwitcher
    private lateinit var minTens: TextSwitcher
    private lateinit var minUnits: TextSwitcher
    private lateinit var secTens: TextSwitcher
    private lateinit var secUnits: TextSwitcher

    private var lastTime: String = ""
    private val handler = Handler(Looper.getMainLooper())
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("Asia/Shanghai")
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dateText = findViewById<TextView>(R.id.dateText)
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        dateText.text = currentDate
        dateText.typeface = resources.getFont(R.font.digital7)
        dateText.setTextColor(resources.getColor(android.R.color.holo_green_light))

        fun initSwitcher(ts: TextSwitcher) {
            ts.setFactory {
                val t = TextView(this)
                t.layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                t.gravity = Gravity.CENTER
                t.textSize = 96f
                t.setTextColor(resources.getColor(android.R.color.holo_green_light))
                t.typeface = resources.getFont(R.font.digital7)
                t
            }
            ts.inAnimation = AnimationUtils.loadAnimation(this, R.anim.flip_in)
            ts.outAnimation = AnimationUtils.loadAnimation(this, R.anim.flip_out)
        }

        hourTens = findViewById(R.id.hourTens)
        hourUnits = findViewById(R.id.hourUnits)
        minTens = findViewById(R.id.minTens)
        minUnits = findViewById(R.id.minUnits)
        secTens = findViewById(R.id.secTens)
        secUnits = findViewById(R.id.secUnits)

        listOf(hourTens, hourUnits, minTens, minUnits, secTens, secUnits).forEach { initSwitcher(it) }

        // 设置初始时间并强制刷新所有位
        lastTime = ""
        val currentTime = timeFormat.format(Date())
        updateClock(currentTime, forceUpdate = true)

        handler.post(updateTimeTask)
    }

    private val updateTimeTask = object : Runnable {
        override fun run() {
            val currentTime = timeFormat.format(Date())
            updateClock(currentTime, forceUpdate = false)
            handler.postDelayed(this, 1000)
        }
    }

    private fun updateClock(newTime: String, forceUpdate: Boolean) {
        for (i in newTime.indices) {
            if (forceUpdate || i >= lastTime.length || newTime[i] != lastTime[i]) {
                when (i) {
                    0 -> hourTens.setText(newTime[0].toString())
                    1 -> hourUnits.setText(newTime[1].toString())
                    3 -> minTens.setText(newTime[3].toString())
                    4 -> minUnits.setText(newTime[4].toString())
                    6 -> secTens.setText(newTime[6].toString())
                    7 -> secUnits.setText(newTime[7].toString())
                }
            }
        }
        lastTime = newTime
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimeTask)
    }
}
