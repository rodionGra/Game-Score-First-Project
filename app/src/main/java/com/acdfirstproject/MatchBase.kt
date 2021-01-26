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
        //TODO
         val listOfMatches = mutableListOf<MatchBase>(
            MatchBase("Name1", "Name1", 1, 3),
            MatchBase("Name2", "Name2", 2, 6),
            MatchBase("Name3", "Name3", 9, 10),
            MatchBase("Name4", "Name4", 2, 1)
        )

        fun getSortedListOfMatches(): List<MatchBase> {
            return listOfMatches.sortedByDescending { it.getMaxPoint() }
        }

        fun cleanAll() {
            listOfMatches.clear()
        }
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


    //TODO
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

