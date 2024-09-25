package com.bs.sriwilis.ui.homepage

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.FragmentHomeBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.ui.homepage.operation.ManageCatalogActivity
import com.bs.sriwilis.ui.homepage.operation.ManageCategoryActivity
import com.bs.sriwilis.ui.homepage.operation.ManageCategoryViewModel
import com.bs.sriwilis.ui.homepage.operation.ManageUserActivity
import com.bs.sriwilis.ui.settings.AdminViewModel
import com.bs.sriwilis.ui.settings.ChangeProfileActivity
import com.bs.sriwilis.utils.ViewModelFactory
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            icProfile.setOnClickListener {
                val intent = Intent(requireContext(), ChangeProfileActivity::class.java)
                startActivity(intent)
            }

            cvManageCategory.setOnClickListener {
                val intent = Intent(requireContext(), ManageCategoryActivity::class.java)
                startActivity(intent)
            }

            cvManageUsers.setOnClickListener {
                val intent = Intent(requireContext(), ManageUserActivity::class.java)
                startActivity(intent)
            }

            cvCatalog.setOnClickListener {
                val intent = Intent(requireContext(), ManageCatalogActivity::class.java)
                startActivity(intent)
            }
        }

        observeAdmin()
        viewModel.fetchAdminDetails()
    }

    private fun observeAdmin() {
        viewModel.adminData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> { }
                is Result.Success -> {

                    val adminDetails = result.data

                    adminDetails?.gambarAdmin?.let { gambarAdmin ->
                        if (gambarAdmin.isNotEmpty()) {
                            val bitmap = decodeBase64ToBitmap(gambarAdmin)
                            if (bitmap != null) {
                                val tempFile = saveBitmapToFile(bitmap)
                                if (tempFile != null) {
                                    Glide.with(requireContext())
                                        .clear(binding.icProfile)

                                    Glide.with(requireContext())
                                        .load(tempFile)
                                        .into(binding.icProfile)
                                } else {
                                    binding.icProfile.setImageResource(R.drawable.ic_profile)
                                }
                            }
                        } else {
                            binding.icProfile.setImageResource(R.drawable.ic_profile)
                        }
                    } ?: run {
                        binding.icProfile.setImageResource(R.drawable.ic_profile)
                    }
                }
                is Result.Error -> {
                }
            }
        })
    }

    private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun saveBitmapToFile(bitmap: Bitmap): File? {
        return try {
            val tempFile = File(requireContext().cacheDir, "temp_image.jpg")
            val fileOutputStream = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            tempFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAdminDetails()
    }

}