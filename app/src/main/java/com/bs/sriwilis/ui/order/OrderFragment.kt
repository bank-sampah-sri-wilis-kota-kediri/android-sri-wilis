package com.bs.sriwilis.ui.order

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.FragmentOrderBinding
import com.bs.sriwilis.ui.settings.ChangePasswordActivity

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/*        binding.cvOrder.setOnClickListener {
            val intent = Intent(context, DetailOrderActivity::class.java)
            startActivity(intent)
        }*/
    }

}