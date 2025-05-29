package com.example.test1clock

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.test1clock.model.AlarmDatabase
import com.example.test1clock.model.AlarmEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlarmActivity : AppCompatActivity() {

    private lateinit var alarmRecyclerView: RecyclerView
    private lateinit var addAlarmButton: ImageButton
    private lateinit var alarmAdapter: AlarmAdapter
    private lateinit var alarmDao: com.example.test1clock.model.AlarmDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        // 初始化 Room 数据库（确保与保存闹钟用的数据库文件名一致！）
        val db = Room.databaseBuilder(
            applicationContext,
            AlarmDatabase::class.java, "alarm-database"
        ).build()
        alarmDao = db.alarmDao()

        // 初始化布局控件
        alarmRecyclerView = findViewById(R.id.alarmRecyclerView)
        addAlarmButton = findViewById(R.id.addAlarmButton)

        alarmRecyclerView.layoutManager = LinearLayoutManager(this)
        alarmAdapter = AlarmAdapter(
            onEditClick = { alarm ->
                val intent = Intent(this, EditAlarmActivity::class.java)
                intent.putExtra("alarmId", alarm.id)
                startActivity(intent)
            },
            onSwitchChange = { alarm, isEnabled ->
                val updatedAlarm = alarm.copy(enabled = isEnabled)
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        alarmDao.updateAlarm(updatedAlarm)
                    }
                }
            },
            onDeleteClick = { alarm ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        alarmDao.deleteAlarm(alarm) // 删除数据库
                    }
                    withContext(Dispatchers.Main) {
                        loadAlarms() // 刷新列表
                    }
                }
            }
        )
        alarmRecyclerView.adapter = alarmAdapter

        // 加载闹钟数据
        loadAlarms()

        // 添加新闹钟按钮
        addAlarmButton.setOnClickListener {
            val intent = Intent(this, EditAlarmActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadAlarms() // 返回后刷新数据
    }

    private fun loadAlarms() {
        alarmDao.getAllAlarms().observe(this) { alarms ->
            alarmAdapter.submitList(alarms)
        }
    }
}
