package com.acdfirstproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

    private lateinit var binding: ActivityGameBinding
    private lateinit var currentMatch: MatchBase

    private val timer: MyTimer = MyTimer(
        {
            binding.timeView.text = convertMillisecondsToHours(it)
        },
        {
            MatchBase.listOfMatches.add(currentMatch)
            WinnerActivity.start(this@GamingActivity, currentMatch)
            this@GamingActivity.finish()
        }
    )

    override fun onStart() {
        timer.continueTimer()
        super.onStart()
    }

    override fun onStop() {
        timer.pauseTimer()
        super.onStop()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupListeners()
        setupData()
        startTimer()
    }

    private fun setupBinding() {
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun startTimer() {
        timer.startTimer(intent.getLongExtra(MILLI_SECOND_FOR_TIMER, 0L))
    }

    private fun setupListeners() {
        binding.btnPauseContinue.setOnClickListener {
            binding.btnPauseContinue.text =
                if (timer.isRunning) resources.getString(R.string.continue_text) else resources.getString(R.string.pause)
            timer.switchTimer()
        }
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

    private fun convertMillisecondsToHours(milliseconds: Long): String {
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
            timer.cancel()
        }
        dialog.show(supportFragmentManager, "FRAGMENT_DIALOG_CANCEL")
    }

    override fun onBackPressed() {
        showDialogCancel()
    }
}