package com.acdfirstproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acdfirstproject.databinding.ActivityGameListBinding

class GameListActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, GameListActivity::class.java)
            context.startActivity(intent)
        }
    }

    lateinit var binding: ActivityGameListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupListeners()
        setupRecycleView()
    }

    private fun setupBinding() {
        binding = ActivityGameListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupListeners() {

    }

    private fun setupRecycleView() {
        binding.recycleViewListOfGame.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycleViewListOfGame.adapter = AdapterGameList(MatchBase.getSortedListOfMatches())
        binding.recycleViewListOfGame.addItemDecoration(
            DividerItemDecoration(this, RecyclerView.VERTICAL)
        )
    }
}