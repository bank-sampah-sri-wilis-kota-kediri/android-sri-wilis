package com.bs.sriwilis.ui.homepage.operation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityAddTransactionBinding
import com.bs.sriwilis.utils.ViewModelFactory
import com.bs.sriwilis.helper.Result

class AddTransaction : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionBinding
    private val viewModel by viewModels<ManageTransactionViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = binding.spinnerNasabahChoose

        getUserPhone()
    }

    private fun getUserPhone() {
        viewModel.nasabah.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    val nameList = result.data
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nameList)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter

                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val selectedPhone = viewModel.getPhoneByPosition(position)
                            Log.d("SelectedPhone", "Phone: $selectedPhone")
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) { }
                    }
                }

                is Result.Error -> {
                    Log.e("Spinner", "Error loading user details: ${result.error}")
                }

                Result.Loading -> {
                    // Show loading indicator
                }
            }
        }
        viewModel.getUserPhones()
    }

}