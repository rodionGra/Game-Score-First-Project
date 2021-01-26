package com.acdfirstproject

import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.acdfirstproject.databinding.ItemRecycleViewBinding

class AdapterGameList(private var list: List<MatchBase>) : RecyclerView.Adapter<AdapterGameList.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recycle_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            list[position].homeTeamName,
            list[position].visitorTeamName,
            list[position].homeTeamPoint,
            list[position].visitorTeamPoint
        )
        holder.itemView.findViewById<ImageView>(R.id.image_view_share).setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "The match between ${list[position].homeTeamName} and ${list[position].visitorTeamName} ended with a score of ${list[position].homeTeamPoint} : ${list[position].visitorTeamPoint}"
                )
                type = "text/plain"
            }
            holder.itemView.context.startActivity(sendIntent)
        }
    }

    fun updateList(newList: List<MatchBase>){
        list = newList
        this.notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemRecycleViewBinding = ItemRecycleViewBinding.bind(itemView)

        fun bind(
            firstTeamName: String,
            secondTeamName: String,
            firstTeamPoint: Int,
            secondTeamPoint: Int
        ) {
            binding.tvFirstTeamName.text = firstTeamName
            binding.tvSecondTeamName.text = secondTeamName
            binding.tvScoreFirstTeam.text = firstTeamPoint.toString()
            binding.tvScoreSecondTeam.text = secondTeamPoint.toString()
        }
    }
}