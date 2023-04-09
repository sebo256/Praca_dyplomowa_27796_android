package com.praca.dyplomowa.android.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
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

        if(SessionManager.getIsAdmin(requireContext())) {
            binding.buttonJobsTimeSpentAdminProfileFragmentView.visibility = View.VISIBLE
            binding.buttonJobsTimeSpentAdminProfileFragmentView.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(android.R.id.content, ProfileUsersListFragmentView())
                    .addToBackStack(null)
                    .commit()
            }
        }

        viewModelProfile = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        setObserverForCountCompletedJobsAppliedToUser()
        setObserverForCountTodoJobsAppliedToUser()
        setObserverForGetUser()
        setObserverForGetSumOfTimeSpentForSpecifiedMonthAndUserAndCheckCompleted()


        binding.buttonLogoutMainActivity.setOnClickListener {
            SessionManager.clearSharedPrefs(requireActivity().applicationContext)
            goToLogin()
        }

        binding.buttonJobsCompletedProfileFragmentView.setOnClickListener {
            goToJobsList(resources.getString(R.string.textview_list_jobs_completed))
        }

        binding.buttonJobsTodoProfileFragmentView.setOnClickListener {
            goToJobsList(resources.getString(R.string.textview_list_jobs_todo))
        }

        binding.buttonJobsTimeSpentProfileFragmentView.setOnClickListener {
            val intent = Intent(requireContext(), ProfileTimeSpentListView::class.java)
            startActivity(intent)
        }



        return view
    }

    private fun setObserverForCountCompletedJobsAppliedToUser(){
        viewModelProfile.jobCompletedCountResult.observe(viewLifecycleOwner){
            binding.textViewJobsCompletedNumberTextProfileFragmentView.setText(it.toString())
        }
        viewModelProfile.countCompletedJobsAppliedToUser(SessionManager.getCurrentUserUsername(requireContext())!!)
    }
    private fun setObserverForCountTodoJobsAppliedToUser(){
        viewModelProfile.jobTodoCountResult.observe(viewLifecycleOwner){
            binding.textViewJobsTodoNumberTextProfileFragmentView.setText(it.toString())
        }
        viewModelProfile.countTodoJobsAppliedToUser(SessionManager.getCurrentUserUsername(requireContext())!!)
    }

    private fun setObserverForGetUser(){
        viewModelProfile.userResponse.observe(viewLifecycleOwner){
            binding.textViewNameProfileFragmentView.setText(it.name)
            binding.textViewSurnameProfileFragmentView.setText(it.surname)
        }
        viewModelProfile.getUser(SessionManager.getCurrentUserUsername(requireContext())!!)
    }

    private fun setObserverForGetSumOfTimeSpentForSpecifiedMonthAndUserAndCheckCompleted(){
        viewModelProfile.jobTimeSpentResult.observe(viewLifecycleOwner){
            binding.textViewJobsTimeSpentNumberTextProfileFragmentView.setText(it.toString() + "h")
        }
        viewModelProfile.getSumOfTimeSpentForSpecifiedMonthAndUserAndCheckCompleted(
            startLong = viewModelProfile.getCurrentMonthBeginLong(),
            endLong = viewModelProfile.getCurrentMonthEndLong(),
            username = SessionManager.getCurrentUserUsername(requireContext())!!)
    }


    private fun goToJobsList(title: String) {
        val intent = Intent(requireContext(), ProfileJobListView::class.java)
        intent.putExtra("title",title)
        startActivity(intent)
    }

    private fun goToLogin() {
        val intent = Intent(requireContext(), LoginActivityView::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }
}