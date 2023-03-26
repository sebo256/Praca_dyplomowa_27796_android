package com.praca.dyplomowa.android.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.FragmentProfileViewBinding
import com.praca.dyplomowa.android.utils.SessionManager

class ProfileFragmentView : Fragment(R.layout.fragment_profile_view) {
    private var _binding: FragmentProfileViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileViewBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.buttonLogoutMainActivity.setOnClickListener {
            SessionManager.clearSharedPrefsTest(requireActivity().applicationContext)
            goToLogin()
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun goToLogin() {
        val intent = Intent(requireContext(), LoginActivityView::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }
}