package com.bs.sriwilis.ui.mutation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bs.sriwilis.databinding.FragmentMutationBinding
import com.bs.sriwilis.ui.settings.ChangePasswordActivity

class MutationFragment : Fragment() {

    private var _binding: FragmentMutationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMutationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartMutation.setOnClickListener {
                val intent = Intent(context, InputMutationActivity::class.java)
                startActivity(intent)
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
