package com.bs.sriwilis.ui.homepage.operation

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityAddTransactionMethodBinding

class AddTransactionMethodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionMethodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnBack.setOnClickListener { finish() }

            cvInputManual.setOnClickListener {
                val intent = Intent(this@AddTransactionMethodActivity, AddTransaction::class.java)
                startActivity(intent)
            }

            cvInputAutomate.setOnClickListener {
                val intent = Intent(this@AddTransactionMethodActivity, AutomatedTransactionListActivity::class.java)
                startActivity(intent)
            }
        }
    }
}