package com.bs.sriwilis.ui.homepage.operation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityAddCartTransaction2Binding

class AddCartTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCartTransaction2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCartTransaction2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnBack.setOnClickListener { finish() }
        }


    }
}