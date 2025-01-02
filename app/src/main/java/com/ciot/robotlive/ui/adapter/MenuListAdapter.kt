package com.ciot.robotlive.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ciot.robotlive.R
import com.ciot.robotlive.bean.RobotData

/**
 * Created by p'c on 2025/1/2.
 * Description:
 * Encoding: utf-8
 */
class MenuListAdapter(private val context: Context, private val robotData: List<RobotData>) : RecyclerView.Adapter<MenuListAdapter.MenuListViewHolder>() {
    private val menuNum = 1
    private var selectedPosition: Int? = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycle_robot_menu, parent, false)
        val holder = MenuListViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: MenuListAdapter.MenuListViewHolder, position: Int) {
        val robotNum = robotData.size
        holder.menuItemTx.text = "All " + robotNum
    }

    override fun getItemCount(): Int {
        return menuNum
    }

    inner class MenuListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var menuItemTx : TextView = itemView.findViewById(R.id.tv_item)
        init {
            menuItemTx.setOnClickListener{
                selectItem(adapterPosition)
            }
        }
    }

    private fun selectItem(position: Int) {
        if (selectedPosition != null) {
            notifyItemChanged(selectedPosition!!)
        }
        selectedPosition = position
        notifyItemChanged(position)
    }
}