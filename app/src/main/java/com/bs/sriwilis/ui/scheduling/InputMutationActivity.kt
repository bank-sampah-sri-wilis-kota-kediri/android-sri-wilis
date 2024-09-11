package com.bs.sriwilis.ui.scheduling

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bs.sriwilis.databinding.ActivityInputMutationBinding

class InputMutationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputMutationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputMutationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}