package com.praca.dyplomowa.android.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.FragmentProfileViewBinding
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.ProfileViewModel

lateinit var viewModelProfile: ProfileViewModel

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

        viewModelProfile = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        setObserverForCountCompletedJobsAppliedToUserAndCompleted()
        setObserverForCountTodoJobsAppliedToUserAndCompleted()
        setObserverForGetUser()

        binding.buttonLogoutMainActivity.setOnClickListener {
            SessionManager.clearSharedPrefs(requireActivity().applicationContext)
            goToLogin()
        }



        return view
    }

    fun setObserverForCountCompletedJobsAppliedToUserAndCompleted(){
        viewModelProfile.jobCompletedCountResult.observe(viewLifecycleOwner){
            binding.textViewJobsCompletedNumberTextProfileFragmentView.setText(it.toString())
        }
        viewModelProfile.countCompletedJobsAppliedToUserAndCheckCompleted(SessionManager.getCurrentUserUsername(requireContext())!!)
    }
    fun setObserverForCountTodoJobsAppliedToUserAndCompleted(){
        viewModelProfile.jobTodoCountResult.observe(viewLifecycleOwner){
            binding.textViewJobsTodoNumberTextProfileFragmentView.setText(it.toString())
        }
        viewModelProfile.countTodoJobsAppliedToUserAndCheckCompleted(SessionManager.getCurrentUserUsername(requireContext())!!)
    }

    fun setObserverForGetUser(){
        viewModelProfile.userResponse.observe(viewLifecycleOwner){
            binding.textViewNameProfileFragmentView.setText(it.name)
            binding.textViewSurnameProfileFragmentView.setText(it.surname)
        }
        viewModelProfile.getUser(SessionManager.getCurrentUserUsername(requireContext())!!)
    }

    private fun goToLogin() {
        val intent = Intent(requireContext(), LoginActivityView::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }
}