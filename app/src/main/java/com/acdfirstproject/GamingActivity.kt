package com.acdfirstproject

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.acdfirstproject.databinding.ActivityGameBinding

class GamingActivity : AppCompatActivity() {

    companion object {
        private const val CHANNEL_ID = "MY_CHANNEL"
        private const val NOTIFICATION_ID = 1

        const val FIRST_TEAM_INTENT: String = "FIRST_TEAM"
        const val SECOND_TEAM_INTENT: String = "SECOND_TEAM"
        const val MILLI_SECOND_FOR_TIMER: String = "MILLI_SECOND_FOR_TIMER"

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
    private lateinit var timer: CustomTimer

    private var isActivityOnPause = false

    override fun onPause() {
        isActivityOnPause = true
        super.onPause()
    }

    override fun onResume() {
        isActivityOnPause = false
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupListeners()
        setupData()
        setupTimer()
    }

    private fun setupBinding() {
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupListeners() {
        binding.btnPauseContinue.setOnClickListener {
            if (timer.isRunning) {
                timer.pause()
                binding.btnPauseContinue.text = resources.getString(R.string.continue_text)
                setGameButtonEnableState(isEnabled = false)
            } else {
                timer.resume()
                binding.btnPauseContinue.text = resources.getString(R.string.pause)
                setGameButtonEnableState(isEnabled = true)
            }
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

    private fun setGameButtonEnableState(isEnabled: Boolean) {
        binding.apply {
            btnMinusPointToFirstTeam.isEnabled = isEnabled
            btnMinusPointToSecondTeam.isEnabled = isEnabled
            btnPlusPointToFirstTeam.isEnabled = isEnabled
            btnPlusPointToSecondTeam.isEnabled = isEnabled
        }
    }

    private fun setupData() {
        currentMatch = MatchBase(
            intent.getStringExtra(FIRST_TEAM_INTENT).orEmpty(),
            intent.getStringExtra(SECOND_TEAM_INTENT).orEmpty()
        )
        binding.tvFirstTeamName.text = currentMatch.homeTeamName
        binding.tvSecondTeamName.text = currentMatch.visitorTeamName
    }

    private fun setupTimer() {
        timer = CustomTimer(
            intent.getLongExtra(MILLI_SECOND_FOR_TIMER, 10_000L), 1_000L,
            onTick = {
                if (!isActivityOnPause) binding.timeView.text = it.convertMillisecondsToHours()
            },
            onFinish = {
                if (isActivityOnPause) {
                    sendNotification()
                } else {
                    WinnerActivity.start(this@GamingActivity, currentMatch)
                }
                MatchBase.listOfMatches.add(currentMatch)
                this@GamingActivity.finish()
            }
        )
        timer.start()
    }

    private fun sendNotification() {
        val intent = Intent(this, WinnerActivity::class.java).apply {
            putExtra(WinnerActivity.RESULT_GAME_INTENT, currentMatch)
            putExtra(WinnerActivity.START_FROM_NOTIFICATION, true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            currentMatch.hashCode(),
            intent,
            0
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.hourglass)
            .setContentTitle("The end of game")
            .setContentText("${currentMatch.homeTeamName} ${currentMatch.homeTeamPoint} : ${currentMatch.visitorTeamPoint} ${currentMatch.visitorTeamName}")
            .addAction(R.drawable.hourglass, "Open result activity", pendingIntent)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        createNotificationChannel()

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "name", importance).apply {
                description = "descriptionText"
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showDialogCancel() {
        val dialog = FragmentDialogCancel.getInstance()
        dialog.isCancelable = false
        dialog.setupResultCallBack {
            timer.stop()
            finish()
        }
        dialog.show(supportFragmentManager, "FRAGMENT_DIALOG_CANCEL")
    }

    override fun onBackPressed() {
        showDialogCancel()
    }
}