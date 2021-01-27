package com.acdfirstproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acdfirstproject.databinding.ActivityGameListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameListActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, GameListActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityGameListBinding
    private var adapter: GameListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupRecycleView()
        setupListeners()
        setupEnabled()
    }

    private fun setupBinding() {
        binding = ActivityGameListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupListeners() {
        binding.btnCleanList.setOnClickListener {
            showDialog()
        }
    }

    private fun setupRecycleView() {
        adapter = GameListAdapter { match ->
            shareMatch(match)
        }
        binding.recycleViewListOfGame.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycleViewListOfGame.adapter = adapter
        binding.recycleViewListOfGame.addItemDecoration(
            DividerItemDecoration(this, RecyclerView.VERTICAL)
        )
        adapter?.updateList(MatchBase.getSortedListOfMatches())
    }

    private fun shareMatch(match: MatchBase) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "The match between ${match.homeTeamName} and ${match.visitorTeamName} ended with a score of ${match.homeTeamPoint} : ${match.visitorTeamPoint}"
            )
            type = "text/plain"
        }
        startActivity(sendIntent)
    }

    private fun setupEnabled() {
        if (MatchBase.listOfMatches.size == 0) {
            binding.btnCleanList.isEnabled = false
        }
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog)
            .setTitle("This can`t be cancel")
            .setMessage("The entire list will be cleared. Do you really want to do this??")
            .setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Clean all list") { _, _ ->
                MatchBase.cleanAll()
                adapter?.updateList(MatchBase.listOfMatches)
                setupEnabled()
            }
            .show()
    }
}