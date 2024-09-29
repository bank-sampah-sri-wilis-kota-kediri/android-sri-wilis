package com.bs.sriwilis.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.FragmentHistoryOrderBinding
import com.bs.sriwilis.databinding.FragmentMutationHistoryBinding
import com.bs.sriwilis.databinding.FragmentOrderFailedBinding

class HistoryMutationFragment : Fragment() {

    private var _binding: FragmentMutationHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMutationHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            cvStatusOrder.setOnClickListener {
                replaceFragment(HistoryOrderFragment())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_history_order, fragment)
            .addToBackStack(null)
            .commit()
    }

}