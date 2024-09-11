package com.bs.sriwilis.ui.scheduling

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bs.sriwilis.databinding.FragmentOrderScheduleBinding

class OrderScheduleFragment : Fragment() {

    private var _binding: FragmentOrderScheduleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
