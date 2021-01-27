package com.acdfirstproject

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.acdfirstproject.databinding.ActivityWinnerBinding
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size


class WinnerActivity : AppCompatActivity() {

    private var startFromNotification: Boolean = true
    private lateinit var binding: ActivityWinnerBinding
    private lateinit var resultOfMatch: MatchBase

    companion object {
        const val RESULT_GAME_INTENT: String = "RESULT_GAME_INTENT"
        const val START_FROM_NOTIFICATION = "START_FROM_NOTIFICATION"

        fun start(context: Context, resultOfMatch: MatchBase) {
            val intent = Intent(context, WinnerActivity::class.java)
            intent.putExtra(RESULT_GAME_INTENT, resultOfMatch)
            intent.putExtra(START_FROM_NOTIFICATION, false)
            context.startActivity(intent)
        }
    }

    override fun onStart() {
        loadConfetti()
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupData()
        setupListeners()
    }

    private fun setupBinding() {
        binding = ActivityWinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupData() {
        resultOfMatch =
            intent.getParcelableExtra(RESULT_GAME_INTENT) ?: MatchBase("empty", "empty", -1, -1)
        startFromNotification = intent.getBooleanExtra(START_FROM_NOTIFICATION, true)

        resultOfMatch.also {
            binding.tvScore.text =
                resources.getString(R.string.score_of_game, it.homeTeamPoint, it.visitorTeamPoint)
            binding.tvFirstTeamName.text = it.homeTeamName
            binding.tvSecondTeamName.text = it.visitorTeamName

            if (it.homeTeamPoint == it.visitorTeamPoint) {
                binding.imgWinner.visibility = View.GONE
                binding.imgLoser.visibility = View.GONE
                binding.imgEquality.visibility = View.VISIBLE
            }
        }
    }

    private fun setupListeners() {
        binding.btnShareResult.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "The match between ${resultOfMatch.homeTeamName} and ${resultOfMatch.visitorTeamName} ended with a score of ${resultOfMatch.homeTeamPoint} : ${resultOfMatch.visitorTeamPoint}"
            )
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }
        binding.btnStartNewGame.setOnClickListener {
            closeActivity()
        }
        binding.btnShowAllGame.setOnClickListener {
            GameListActivity.start(this)
        }
    }

    private fun closeActivity() {
        if (startFromNotification) {
            MainActivity.start(this)
        }
        finish()
    }

    override fun onBackPressed() {
        closeActivity()
    }

    private fun loadConfetti() {
        binding.viewKonfetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(5000L)
            .addShapes(Shape.RECT, Shape.CIRCLE)
            .addSizes(Size(12))
            .setPosition(-50f, binding.viewKonfetti.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)
    }
}