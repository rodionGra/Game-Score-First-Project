package com.acdfirstproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.acdfirstproject.databinding.FragmentDialogCancelBinding

class FragmentDialogCancel : DialogFragment() {

    private lateinit var binding: FragmentDialogCancelBinding

    companion object {
        fun getInstance(): FragmentDialogCancel {
            return FragmentDialogCancel()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogCancelBinding.inflate(inflater, container, false)
        setupListeners()
        return binding.root
    }

    private var resultCallBack: ((Boolean) -> (Unit))? = null
    fun setupResultCallBack(resultCallBack: ((Boolean) -> (Unit))) {
        this.resultCallBack = resultCallBack
    }
    private fun setupListeners() {
        binding.btnStopGame.setOnClickListener {
            resultCallBack?.invoke(false)
            this.activity?.finish()
        }
        binding.btnContinue.setOnClickListener {
            this.dialog?.cancel()
        }
    }
}