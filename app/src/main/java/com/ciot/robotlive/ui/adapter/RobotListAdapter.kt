package com.ciot.robotlive.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ciot.robotlive.R
import com.ciot.robotlive.bean.RobotData
import com.ciot.robotlive.ui.widgets.CustomClickListener

/**
 * Created by p'c on 2025/1/2.
 * Description:
 * Encoding: utf-8
 */
class RobotListAdapter(private val context: Context, private val robotData: List<RobotData>) : RecyclerView.Adapter<RobotListAdapter.RobotListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RobotListAdapter.RobotListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycle_robot_list, parent, false)
        return RobotListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return robotData.size
    }

    override fun onBindViewHolder(holder: RobotListAdapter.RobotListViewHolder, position: Int) {
        holder.robotId.text = robotData[position].name
        holder.location.text = robotData[position].id
        if (!robotData[position].link) {
            holder.ivOnline.visibility = View.INVISIBLE
        }
        holder.ctlButton.setOnClickListener(object : CustomClickListener() {
            override fun onSingleClick(v: View) {
                ctlButtonClickListener?.onCtlClick(holder.adapterPosition)
            }
        })
    }

    inner class RobotListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val robotId: TextView = itemView.findViewById(R.id.tv_robot_id)
        val location: TextView = itemView.findViewById(R.id.tv_robot_location)
        val ctlButton: Button = itemView.findViewById(R.id.bt_ctl)
        val ivOnline: ImageView = itemView.findViewById(R.id.iv_Online)
    }

    interface OnCtlClickListener {
        fun onCtlClick(position: Int)
    }

    fun setCtlButtonClickListener(listener: OnCtlClickListener) {
        this.ctlButtonClickListener = listener
    }

    private var ctlButtonClickListener: OnCtlClickListener? = null
}