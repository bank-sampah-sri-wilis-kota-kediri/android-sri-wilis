package com.bs.sriwilis.ui.homepage.operation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.UserAdapter
import com.bs.sriwilis.data.preference.UserPreferences
import com.bs.sriwilis.data.preference.dataStore
import com.bs.sriwilis.databinding.ActivityAddUserBinding
import com.bs.sriwilis.databinding.ActivityManageUserBinding
import com.bs.sriwilis.utils.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.bs.sriwilis.helper.Result

class ManageUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageUserBinding
    private val viewModel by viewModels<ManageUserViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var token: String? = null
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter(emptyList())

        binding.apply {
            fabAddUser.setOnClickListener {
                val intent = Intent(this@ManageUserActivity, AddUserActivity::class.java)
                startActivity(intent)
            }
            btnBack.setOnClickListener { finish() }
        }
        observeUser()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = userAdapter

        val userPreferences = UserPreferences.getInstance(this.dataStore)
        lifecycleScope.launch {
            token = userPreferences.token.first()
            token?.let {
                viewModel.getUsers()
            }
        }
    }

    private fun observeUser() {
        viewModel.users.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val userData = result.data.data ?: emptyList()
                    userAdapter.updateUsers(userData)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
        viewModel.getUsers()
    }
}