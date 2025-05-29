package com.example.test1clock

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import android.widget.ImageView
import org.json.JSONObject
import java.net.URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var hourTens: TextSwitcher
    private lateinit var hourUnits: TextSwitcher
    private lateinit var minTens: TextSwitcher
    private lateinit var minUnits: TextSwitcher
    private lateinit var secTens: TextSwitcher
    private lateinit var secUnits: TextSwitcher
    private lateinit var weatherIcon: ImageView
    private lateinit var weatherText: TextView

    private val apiKey = "1a93f36bbcbf88e1bb546c50edb79f74"
    private val city = "Beijing"

    private var lastTime: String = ""
    private val handler = Handler(Looper.getMainLooper())
    //使用北京时区
    /*private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("Asia/Shanghai")
    }*/
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化天气控件
        weatherIcon = findViewById(R.id.weatherIcon)
        weatherText = findViewById(R.id.weatherText)

        // 加载天气
        fetchWeather()

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

        val btnAlarm = findViewById<ImageButton>(R.id.btnAlarm)
        val btnClock = findViewById<ImageButton>(R.id.btnClock)

        btnAlarm.setOnClickListener {
            val intent = Intent(this, AlarmActivity::class.java)
            startActivity(intent)
        }

        btnClock.setOnClickListener {
            val intent = Intent(this, ClockActivity::class.java)
            startActivity(intent)
        }
    }

    //天气显示
    private fun fetchWeather() {
        // 启动协程，使用 IO 线程处理网络请求
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = URL("https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric&lang=zh_cn").readText()
                val json = JSONObject(response)
                val weather = json.getJSONArray("weather").getJSONObject(0)
                val description = weather.getString("description")
                val icon = weather.getString("icon")
                val main = json.getJSONObject("main")
                val temp = main.getDouble("temp")

                // 切换到主线程更新 UI
                withContext(Dispatchers.Main) {
                    weatherText.text = "${description} ${temp}°C"
                    val iconResId = getIconResource(icon)
                    weatherIcon.setImageResource(iconResId)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    weatherText.text = "加载失败"
                }
            }
        }
    }

    private fun getIconResource(iconCode: String): Int {
        return when (iconCode) {
            "01d" -> R.drawable.ic_clear_day
            "01n" -> R.drawable.ic_clear_night
            "02d", "02n" -> R.drawable.ic_partly_cloudy
            "03d", "03n", "04d", "04n" -> R.drawable.ic_cloudy
            "09d", "09n", "10d", "10n" -> R.drawable.ic_rain
            "11d", "11n" -> R.drawable.ic_thunderstorm
            "13d", "13n" -> R.drawable.ic_snow
            "50d", "50n" -> R.drawable.ic_fog
            else -> R.drawable.ic_weather_placeholder
        }
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
