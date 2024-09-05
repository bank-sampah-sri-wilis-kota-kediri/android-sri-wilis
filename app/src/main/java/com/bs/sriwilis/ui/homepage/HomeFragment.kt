package com.bs.sriwilis.ui.homepage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bs.sriwilis.databinding.FragmentHomeBinding
import com.bs.sriwilis.ui.homepage.operation.ManageCatalogActivity
import com.bs.sriwilis.ui.homepage.operation.ManageCategoryActivity
import com.bs.sriwilis.ui.homepage.operation.ManageUserActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

}