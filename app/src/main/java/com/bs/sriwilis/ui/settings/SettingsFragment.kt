package com.bs.sriwilis.ui.settings

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bs.sriwilis.databinding.FragmentSettingsBinding
import com.bs.sriwilis.ui.homepage.HomepageActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            cvProfile.setOnClickListener {
                val intent = Intent(context, ChangeProfileActivity::class.java)
                startActivity(intent)
            }
            cvChangePassword.setOnClickListener {
                val intent = Intent(context, ChangePasswordActivity::class.java)
                startActivity(intent)
            }
            cvPengaturanTransaksi.setOnClickListener {
                TransactionSettingsFragment().show(parentFragmentManager, "transactionSettings")
            }
            cvLogout.setOnClickListener {
                showLogoutDialog()
            }
        }
    }

    fun showLogoutDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Keluar Akun")
        builder.setMessage("Yakin ingin keluar dari akun?")

        builder.setPositiveButton("Ya") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("Tidak") { dialogInterface, _ ->
            // Dismiss the dialog
            dialogInterface.dismiss()
        }

        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
