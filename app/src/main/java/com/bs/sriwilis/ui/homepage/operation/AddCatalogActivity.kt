package com.bs.sriwilis.ui.homepage.operation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityAddCatalogBinding
import com.bs.sriwilis.databinding.ActivityLoginBinding

class AddCatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCatalogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnBack.setOnClickListener { finish() }
        }
    }
}