package com.acdfirstproject

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.acdfirstproject.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val s1: String = binding.teFirstTeam.text.toString().trim()
            val s2: String = binding.teSecondTeam.text.toString().trim()
            binding.btnStartMatch.isEnabled = s1.isNotEmpty() && s2.isNotEmpty()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupListeners()
        setupTimePicker()
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupTimePicker() {
        binding.timePicker.apply {
            setIs24HourView(true)
            hour = 0
            minute = 1
        }
    }

    private fun setupListeners() {
        binding.teFirstTeam.addTextChangedListener(textWatcher)
        binding.teSecondTeam.addTextChangedListener(textWatcher)

        binding.btnStartMatch.setOnClickListener {
            startMatch()
        }
        binding.btnShowGameList.setOnClickListener {
            GameListActivity.start(this)
        }

        binding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            if (hourOfDay == 0 && minute == 0) {
                binding.timePicker.minute = 1
            }
        }
    }

    private fun startMatch() {
        val minute = (binding.timePicker.hour * 60) + binding.timePicker.minute
        val milliSeconds: Long = (minute * 60 * 1000).toLong()

        GamingActivity.start(
            this,
            binding.teFirstTeam.text.toString(),
            binding.teSecondTeam.text.toString(),
            milliSeconds
        )

        binding.teFirstTeam.setText("")
        binding.teSecondTeam.setText("")
    }

    private var backPressedTime: Long = 0L
    private var backToast: Toast? = null
    override fun onBackPressed() {
        if (backPressedTime + 2000L >= System.currentTimeMillis()) {
            super.onBackPressed()
            backToast?.cancel()
        } else {
            backToast = Toast.makeText(baseContext, "Click again for exit!", Toast.LENGTH_SHORT)
            backToast?.show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}