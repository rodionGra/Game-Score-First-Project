package com.acdfirstproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acdfirstproject.databinding.ItemRecycleViewBinding

class GameListAdapter(private val onShareClick: (MatchBase) -> Unit) :
    RecyclerView.Adapter<GameListAdapter.ViewHolder>() {

    private var list = mutableListOf<MatchBase>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_recycle_view, parent, false)
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
        holder.setupShareClick(onShareClick)
    }

    fun updateList(newList: List<MatchBase>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemRecycleViewBinding = ItemRecycleViewBinding.bind(itemView)
        private var matchBase: MatchBase? = null

        fun bind(
            matchBase: MatchBase
        ) {
            binding.tvFirstTeamName.text = matchBase.homeTeamName
            binding.tvSecondTeamName.text = matchBase.visitorTeamName
            binding.tvScoreFirstTeam.text = matchBase.homeTeamPoint.toString()
            binding.tvScoreSecondTeam.text = matchBase.visitorTeamPoint.toString()
        }

        fun setupShareClick(onShareClick: (MatchBase) -> Unit) {
            binding.imageViewShare.setOnClickListener {
                matchBase?.also { match ->
                    onShareClick.invoke(match)
                }
            }
        }
    }
}