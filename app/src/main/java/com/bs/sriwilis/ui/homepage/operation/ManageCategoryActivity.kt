package com.bs.sriwilis.ui.homepage.operation

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityAddUserBinding
import com.bs.sriwilis.databinding.ActivityManageCategoryBinding

class ManageCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            fabAddCategory.setOnClickListener {
                val intent = Intent(this@ManageCategoryActivity, AddCategoryActivity::class.java)
                startActivity(intent)
            }
            btnBack.setOnClickListener {
                finish()
            }
        }
    }
}