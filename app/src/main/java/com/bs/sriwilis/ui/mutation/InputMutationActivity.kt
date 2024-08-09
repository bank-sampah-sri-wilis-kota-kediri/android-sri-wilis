package com.bs.sriwilis.ui.mutation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityChangePasswordBinding
import com.bs.sriwilis.databinding.ActivityInputMutationBinding

class InputMutationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputMutationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputMutationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}