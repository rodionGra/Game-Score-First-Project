package com.acdfirstproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.acdfirstproject.databinding.ActivityGameBinding
import java.util.*
import java.util.concurrent.TimeUnit

class GamingActivity : AppCompatActivity() {

    companion object {
        const val FIRST_TEAM_INTENT: String = "FIRST_TEAM"
        const val SECOND_TEAM_INTENT: String = "SECOND_TEAM"
        const val MILLI_SECOND_FOR_TIMER = "MILLI_SECOND_FOR_TIMER"

        fun start(
            context: Context,
            firstTeamName: String,
            secondTeamName: String,
            milliSeconds: Long
        ) {
            val intent = Intent(context, GamingActivity::class.java)
            intent.putExtra(FIRST_TEAM_INTENT, firstTeamName)
            intent.putExtra(SECOND_TEAM_INTENT, secondTeamName)
            intent.putExtra(MILLI_SECOND_FOR_TIMER, milliSeconds)
            context.startActivity(intent)
        }


    }

    var isRunning = true
    private lateinit var countDownTimer: CountDownTimer
    private fun startTimer(time_in_milliSeconds: Long) {
        countDownTimer = object : CountDownTimer(time_in_milliSeconds, 1000) {
            override fun onFinish() {
                MatchBase.listOfMatches.add(currentMatch)
                WinnerActivity.start(this@GamingActivity, currentMatch)
                this@GamingActivity.finish()
            }
            override fun onTick(millisUntilFinished: Long) {
                if (isRunning) {
                    Log.d("TAG", "onTick")
                    binding.timeView.text = convertMillisecondsToHours(millisUntilFinished)
                }else this.cancel()
            }
        }.start()
    }

    private lateinit var binding: ActivityGameBinding
    private lateinit var currentMatch: MatchBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupListeners()
        setupData()
        startTimer(intent.getLongExtra(MILLI_SECOND_FOR_TIMER, 30_000L))
    }

    private fun setupBinding() {
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupListeners() {
        binding.btnPlusPointToFirstTeam.setOnClickListener {
            currentMatch.incrementFirstTeamPoint()
            binding.tvScoreFirstTeam.text = currentMatch.homeTeamPoint.toString()
        }
        binding.btnPlusPointToSecondTeam.setOnClickListener {
            currentMatch.incrementSecondTeamPoint()
            binding.tvScoreSecondTeam.text = currentMatch.visitorTeamPoint.toString()
        }
        binding.btnMinusPointToFirstTeam.setOnClickListener {
            currentMatch.decrementFirstTeamPoint()
            binding.tvScoreFirstTeam.text = currentMatch.homeTeamPoint.toString()
        }
        binding.btnMinusPointToSecondTeam.setOnClickListener {
            currentMatch.decrementSecondTeamPoint()
            binding.tvScoreSecondTeam.text = currentMatch.visitorTeamPoint.toString()
        }
        binding.btnCancel.setOnClickListener {
            showDialogCancel()
        }
    }

    private fun setupData() {
        currentMatch = MatchBase(
            intent.getStringExtra(FIRST_TEAM_INTENT).toString(),
            intent.getStringExtra(SECOND_TEAM_INTENT).toString()
        )
        binding.tvFirstTeamName.text = currentMatch.homeTeamName
        binding.tvSecondTeamName.text = currentMatch.visitorTeamName
    }

    fun convertMillisecondsToHours(milliseconds: Long): String {
        var milliSeconds = milliseconds
        val hours = TimeUnit.MILLISECONDS.toHours(milliSeconds)
        milliSeconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds)
        milliSeconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds)

        return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
    }

    private fun showDialogCancel() {
        val dialog = FragmentDialogCancel.getInstance()
        dialog.isCancelable = false
        dialog.setupResultCallBack {
            this.isRunning = it
        }
        dialog.show(supportFragmentManager, "tag")
    }

    override fun onBackPressed() {
        showDialogCancel()
    }
}