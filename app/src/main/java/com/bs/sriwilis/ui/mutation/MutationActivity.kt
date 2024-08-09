package com.bs.sriwilis.ui.mutation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityChangePasswordBinding
import com.bs.sriwilis.databinding.ActivityMutationBinding

class MutationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMutationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMutationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}