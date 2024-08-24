package com.bs.sriwilis.ui.homepage

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityHomepageBinding
import com.bs.sriwilis.ui.mutation.MutationFragment
import com.bs.sriwilis.ui.order.OrderFragment
import com.bs.sriwilis.ui.settings.SettingsFragment

class HomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    if (!isCurrentFragment(HomeFragment::class.java)) {
                        replaceFragment(HomeFragment())
                    }
                    true
                }
                R.id.order -> {
                    if (!isCurrentFragment(OrderFragment::class.java)) {
                        replaceFragment(OrderFragment())
                    }
                    true
                }
                R.id.mutation -> {
                    if (!isCurrentFragment(MutationFragment::class.java)) {
                        replaceFragment(MutationFragment())
                    }
                    true
                }
                R.id.settings -> {
                    if (!isCurrentFragment(SettingsFragment::class.java)) {
                        replaceFragment(SettingsFragment())
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun isCurrentFragment(fragmentClass: Class<out Fragment>): Boolean {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        return currentFragment != null && currentFragment::class.java == fragmentClass
    }
}