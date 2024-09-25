package com.bs.sriwilis.ui.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bs.sriwilis.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}