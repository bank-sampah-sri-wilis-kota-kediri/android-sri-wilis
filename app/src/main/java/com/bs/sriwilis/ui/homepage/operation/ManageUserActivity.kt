package com.bs.sriwilis.ui.homepage.operation

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.UserAdapter
import com.bs.sriwilis.databinding.ActivityAddUserBinding
import com.bs.sriwilis.databinding.ActivityManageUserBinding
import com.bs.sriwilis.utils.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.bs.sriwilis.helper.Result
import kotlinx.coroutines.runBlocking

class ManageUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageUserBinding
    private val viewModel by viewModels<ManageUserViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter(emptyList(), this)

        binding.apply {
            fabAddUser.setOnClickListener {
                val intent = Intent(this@ManageUserActivity, AddUserActivity::class.java)
                startActivity(intent)
            }
            btnBack.setOnClickListener { finish() }

            swipeRefreshLayout.setOnRefreshListener {
                lifecycleScope.launch {
                    viewModel.syncData()
                    viewModel.getUsers()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.getUsers()
            observeUser()
        }
        setupRecyclerView()
        setupSearchBar()
    }

    private fun setupRecyclerView() {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = userAdapter
    }

    private suspend fun observeUser() {
        viewModel.nasabah.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                    val userData = result.data
                        lifecycleScope.launch {
                            viewModel.syncData()
                        }
                    userAdapter.updateUsers(userData)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }

        viewModel.searchResults.observe(this, Observer { searchResult ->
            userAdapter.updateUsers(searchResult)
        })

        viewModel.getUsers()
    }

    private fun setupSearchBar() {
        binding.searchBar.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.toString().isEmpty()) {
                        lifecycleScope.launch {
                            viewModel.getUsers()
                        }
                    } else {
                        searchNasabahByName(it.toString())
                    }
                }
            }

        })
    }

    private fun searchNasabahByName(name: String) {
        lifecycleScope.launch {
            viewModel.searchUsers(name = name)
        }
    }


}

