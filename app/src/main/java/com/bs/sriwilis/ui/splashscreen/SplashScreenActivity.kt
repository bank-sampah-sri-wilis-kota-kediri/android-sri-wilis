package com.bs.sriwilis.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityChangeProfileBinding
import com.bs.sriwilis.databinding.ActivitySplashScreenBinding
import com.bs.sriwilis.ui.homepage.HomepageActivity
import com.bs.sriwilis.ui.settings.ChangePasswordActivity
import com.bs.sriwilis.ui.settings.ChangeProfileActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAuth.setOnClickListener {
            val intent = Intent(this, ChangeProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}