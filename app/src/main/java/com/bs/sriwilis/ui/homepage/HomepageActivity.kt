package com.bs.sriwilis.ui.homepage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityHomepageBinding
import com.bs.sriwilis.ui.history.HistoryOrderFragment
import com.bs.sriwilis.ui.homepage.operation.AddTransaction
import com.bs.sriwilis.ui.homepage.operation.AddTransactionMethodActivity
import com.bs.sriwilis.ui.scheduling.OrderScheduleFragment
import com.bs.sriwilis.ui.scheduling.OrderUnscheduledFragment
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
                    if (!isCurrentFragment(HistoryOrderFragment::class.java)) {
                        replaceFragment(HistoryOrderFragment())
                    }
                    true
                }
                R.id.mutation -> {
                    if (!isCurrentFragment(OrderUnscheduledFragment::class.java)) {
                        replaceFragment(OrderUnscheduledFragment())
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

        binding.fabAddOrder.setOnClickListener {
            val intent = Intent(this, AddTransactionMethodActivity::class.java)
            startActivity(intent)

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