package com.example.test1clock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.test1clock.model.AlarmEntity

class AlarmAdapter(
    private val onEditClick: (AlarmEntity) -> Unit,
    private val onSwitchChange: (AlarmEntity, Boolean) -> Unit,
    private val onDeleteClick: (AlarmEntity) -> Unit // 新增回调
) : ListAdapter<AlarmEntity, AlarmAdapter.AlarmViewHolder>(AlarmDiffCallback()) {

    inner class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeText: TextView = itemView.findViewById(R.id.timeText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val repeatText: TextView = itemView.findViewById(R.id.repeatText)
        val switch: Switch = itemView.findViewById(R.id.alarmSwitch)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton) // 新增
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = getItem(position)
        holder.timeText.text = alarm.time
        holder.dateText.text = alarm.date
        holder.repeatText.text = alarm.repeat
        holder.switch.isChecked = alarm.enabled

        holder.itemView.setOnClickListener { onEditClick(alarm) }
        holder.switch.setOnCheckedChangeListener { _, isChecked -> onSwitchChange(alarm, isChecked) }
        holder.deleteButton.setOnClickListener { onDeleteClick(alarm) } // 绑定删除事件
    }
}

class AlarmDiffCallback : DiffUtil.ItemCallback<AlarmEntity>() {
    override fun areItemsTheSame(oldItem: AlarmEntity, newItem: AlarmEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AlarmEntity, newItem: AlarmEntity): Boolean {
        return oldItem == newItem
    }
}
