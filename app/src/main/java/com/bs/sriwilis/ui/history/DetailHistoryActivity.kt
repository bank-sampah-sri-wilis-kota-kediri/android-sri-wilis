package com.bs.sriwilis.ui.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bs.sriwilis.databinding.ActivityDetailOrderBinding

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}