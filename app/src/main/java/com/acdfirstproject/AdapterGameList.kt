package com.acdfirstproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterGameList(private val mutableList: MutableList<MatchBase>) : RecyclerView.Adapter<AdapterGameList.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recycle_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            mutableList[position].homeTeamName,
            mutableList[position].visitorTeamName,
            mutableList[position].homeTeamPoint,
            mutableList[position].visitorTeamPoint
        )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            firstTeamName: String,
            secondTeamName: String,
            firstTeamPoint: Int,
            secondTeamPoint: Int
        ) {
            itemView.findViewById<TextView>(R.id.tv_first_team_name).text = firstTeamName
            itemView.findViewById<TextView>(R.id.tv_second_team_name).text = secondTeamName
            itemView.findViewById<TextView>(R.id.tv_score_first_team).text = firstTeamPoint.toString()
            itemView.findViewById<TextView>(R.id.tv_score_second_team).text = secondTeamPoint.toString()
        }
    }
}