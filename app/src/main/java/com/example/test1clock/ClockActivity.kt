package com.example.test1clock

import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ClockActivity : AppCompatActivity() {

    private lateinit var chronometer: Chronometer
    private lateinit var startStopButton: ImageButton
    private lateinit var resetButton: ImageButton
    private var running = false
    private var pauseOffset: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock)

        chronometer = findViewById(R.id.chronometer)
        startStopButton = findViewById(R.id.startStopButton)
        resetButton = findViewById(R.id.resetButton)

        startStopButton.setOnClickListener {
            if (running) {
                chronometer.stop()
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
                startStopButton.setImageResource(android.R.drawable.ic_media_play)
                running = false
            } else {
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
                chronometer.start()
                startStopButton.setImageResource(android.R.drawable.ic_media_pause)
                running = true
            }
        }

        resetButton.setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime()
            pauseOffset = 0
            if (!running) {
                chronometer.stop()
            }
        }
    }
}
