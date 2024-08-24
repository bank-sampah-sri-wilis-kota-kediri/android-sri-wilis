package com.bs.sriwilis.ui.homepage.operation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityAddUserBinding
import com.bs.sriwilis.databinding.ActivityEditCatalogBinding

class EditCatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditCatalogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding?.apply {

        }
    }
}