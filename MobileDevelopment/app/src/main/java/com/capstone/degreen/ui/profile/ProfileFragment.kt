package com.capstone.degreen.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.degreen.databinding.FragmentProfileBinding
import com.capstone.degreen.ui.aboutUs.AboutUsActivity
import com.capstone.degreen.ui.autent.login.LoginActivity


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Mengambil nama pengguna dari SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("preferensi aplikasi", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("user_name", "GUEST")



        // Tampilkan nama pengguna di TextView atau elemen UI lainnya
        val textViewUsername = binding.tvname
        textViewUsername.text = username

        val btnAboutUs: Button = binding.btnAboutus
        btnAboutUs.setOnClickListener{
            val intent = Intent(requireContext(), AboutUsActivity::class.java)
            startActivity(intent)
        }

        val btnlogout: Button = binding.btnLogout
        btnlogout.setOnClickListener {
            val intent = Intent(requireContext(),LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}