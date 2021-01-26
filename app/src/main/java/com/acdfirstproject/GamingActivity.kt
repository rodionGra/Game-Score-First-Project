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
import java.util.*
import java.util.concurrent.TimeUnit


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
            }
            else{
                timer.resume()
                binding.btnPauseContinue.text = resources.getString(R.string.pause)
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

    private fun setupData() {
        currentMatch = MatchBase(
            intent.getStringExtra(FIRST_TEAM_INTENT).toString(),
            intent.getStringExtra(SECOND_TEAM_INTENT).toString()
        )
        binding.tvFirstTeamName.text = currentMatch.homeTeamName
        binding.tvSecondTeamName.text = currentMatch.visitorTeamName
    }

    private lateinit var timer: CustomTimer
    private fun setupTimer() {
        val milliTillFinished = intent.getLongExtra(MILLI_SECOND_FOR_TIMER, 10_000L)
        timer = CustomTimer(
            {
                if (!isActivityOnPause) binding.timeView.text = convertMillisecondsToHours(it)
            },
            {
                if (isActivityOnPause) {
                    sendNotification()
                    MatchBase.listOfMatches.add(currentMatch)
                    this@GamingActivity.finish()
                } else {
                    MatchBase.listOfMatches.add(currentMatch)
                    WinnerActivity.start(this@GamingActivity, currentMatch)
                    this@GamingActivity.finish()
                }
            }, milliTillFinished, 1_000L
        )
        timer.start()
    }

    private fun sendNotification() {
        val intent = Intent(this, WinnerActivity::class.java).apply{
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
            timer.stop()
        }
        dialog.show(supportFragmentManager, "FRAGMENT_DIALOG_CANCEL")
    }

    override fun onBackPressed() {
        showDialogCancel()
    }
}