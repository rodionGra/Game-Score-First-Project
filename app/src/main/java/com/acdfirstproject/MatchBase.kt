package com.acdfirstproject

import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MatchBase(
    val homeTeamName: String,
    val visitorTeamName: String,
    var homeTeamPoint: Int = 0,
    var visitorTeamPoint: Int = 0
) : Parcelable {
    companion object {
        @JvmStatic
        val listOfMatches = mutableListOf<MatchBase>(
            MatchBase("Name1", "Name1", 1, 3),
            MatchBase("Name2", "Name2", 2, 6),
            MatchBase("Name3", "Name3", 9, 10),
            MatchBase("Name4", "Name4", 2, 1)
        )

        fun getSortedListOfMatches(): MutableList<MatchBase> {
            return listOfMatches.sortedByDescending { it.getMaxPoint() }.toMutableList()
        }

        //TODO CLEAN UP
        /*val list : SortedList<MatchBase> = SortedList(MatchBase::class.java, object :
            SortedListAdapterCallback<MatchBase>(AdapterGameList()) {
            override fun compare(o1: MatchBase, o2: MatchBase): Int {
                return o1.getMaxPoint() - o2.getMaxPoint()
            }

            override fun areContentsTheSame(oldItem: MatchBase?, newItem: MatchBase?): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(item1: MatchBase?, item2: MatchBase?): Boolean {
                return item1 == item2
            }
        })*/
    }

    fun incrementFirstTeamPoint() {
        homeTeamPoint++
    }

    fun incrementSecondTeamPoint() {
        visitorTeamPoint++
    }

    fun decrementFirstTeamPoint() {
        if (homeTeamPoint > 0) {
            homeTeamPoint--
        }
    }

    fun decrementSecondTeamPoint() {
        if (visitorTeamPoint > 0) {
            visitorTeamPoint--
        }
    }

    fun getWinnerTeamName(): String {
        return when {
            homeTeamPoint > visitorTeamPoint -> homeTeamName
            homeTeamPoint < visitorTeamPoint -> visitorTeamName
            else -> "Draw"
        }
    }

    fun getMaxPoint(): Int {
        return if (homeTeamPoint > visitorTeamPoint) homeTeamPoint else visitorTeamPoint
    }

}

