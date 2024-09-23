package com.bs.sriwilis

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bs.sriwilis.databinding.ActivityAddCartTransactionBinding

class AddCartTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCartTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCartTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

        }
    }
}