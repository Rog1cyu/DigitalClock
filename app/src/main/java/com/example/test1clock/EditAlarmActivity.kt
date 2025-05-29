package com.example.test1clock

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.test1clock.model.AlarmDatabase
import com.example.test1clock.model.AlarmEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class EditAlarmActivity : AppCompatActivity() {
    private lateinit var selectTimeButton: Button
    private lateinit var selectDateButton: Button
    private lateinit var repeatGroup: RadioGroup
    private lateinit var alarmSwitch: Switch
    private lateinit var saveAlarmButton: Button

    private val calendar = Calendar.getInstance()
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    private var alarmId: Int? = null // 区分新增和编辑

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_alarm)

        selectTimeButton = findViewById(R.id.selectTimeButton)
        selectDateButton = findViewById(R.id.selectDateButton)
        repeatGroup = findViewById(R.id.repeatGroup)
        alarmSwitch = findViewById(R.id.alarmSwitch)
        saveAlarmButton = findViewById(R.id.saveAlarmButton)

        val db = Room.databaseBuilder(
            applicationContext,
            AlarmDatabase::class.java,
            "alarm-database"
        ).build()
        val alarmDao = db.alarmDao()

        // 检查是否是编辑模式
        alarmId = intent.getIntExtra("alarmId", -1).takeIf { it != -1 }
        if (alarmId != null) {
            lifecycleScope.launch {
                val alarm = withContext(Dispatchers.IO) { alarmDao.getAlarmById(alarmId!!) }
                alarm?.let {
                    // 填充 UI
                    selectTimeButton.text = "时间: ${it.time}"
                    selectDateButton.text = "日期: ${it.date}"
                    alarmSwitch.isChecked = it.enabled
                    calendar.time = dateFormat.parse(it.date)!!
                    val timeParts = it.time.split(":")
                    calendar.set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())
                    calendar.set(Calendar.MINUTE, timeParts[1].toInt())

                    when (it.repeat) {
                        "每天" -> repeatGroup.check(R.id.repeatDaily)
                        "每周" -> repeatGroup.check(R.id.repeatWeekly)
                        "每月" -> repeatGroup.check(R.id.repeatMonthly)
                        else -> repeatGroup.check(R.id.repeatNone)
                    }
                }
            }
        }

        selectTimeButton.setOnClickListener {
            TimePickerDialog(this, { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                selectTimeButton.text = "时间: ${timeFormat.format(calendar.time)}"
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        selectDateButton.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                selectDateButton.text = "日期: ${dateFormat.format(calendar.time)}"
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        saveAlarmButton.setOnClickListener {
            val repeatOption = when (repeatGroup.checkedRadioButtonId) {
                R.id.repeatDaily -> "每天"
                R.id.repeatWeekly -> "每周"
                R.id.repeatMonthly -> "每月"
                else -> "不重复"
            }
            val isEnabled = alarmSwitch.isChecked
            val time = timeFormat.format(calendar.time)
            val date = dateFormat.format(calendar.time)

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    if (alarmId != null) {
                        // 编辑模式：更新闹钟
                        val updatedAlarm = AlarmEntity(
                            id = alarmId!!,
                            time = time,
                            date = date,
                            repeat = repeatOption,
                            enabled = isEnabled
                        )
                        alarmDao.updateAlarm(updatedAlarm)
                    } else {
                        // 新增模式：插入新闹钟
                        val newAlarm = AlarmEntity(
                            time = time,
                            date = date,
                            repeat = repeatOption,
                            enabled = isEnabled
                        )
                        alarmDao.insertAlarm(newAlarm)
                    }
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@EditAlarmActivity,
                        "闹钟已保存",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }
}
