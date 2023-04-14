package com.praca.dyplomowa.android.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.FragmentProfileTimeSpentListViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.ProfileTimeSpentListViewModel
import com.praca.dyplomowa.android.views.adapters.ProfileTimeSpentAdapter

class ProfileTimeSpentListFragmentView : Fragment() {

    private var _binding: FragmentProfileTimeSpentListViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelProfileTimeSpentList: ProfileTimeSpentListViewModel
    private lateinit var timeSpentAdapter: ProfileTimeSpentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileTimeSpentListViewBinding.inflate(inflater, container, false)

        viewModelProfileTimeSpentList = ViewModelProvider(this).get(ProfileTimeSpentListViewModel::class.java)
        setObserverForError()
        setObserverForGetAllTimeSpentForUserPerMonth()

        binding.recyclerTimeSpent.layoutManager = LinearLayoutManager(requireContext())
        timeSpentAdapter = ProfileTimeSpentAdapter()
        binding.recyclerTimeSpent.adapter = timeSpentAdapter

        return binding.root
    }

    private fun setObserverForGetAllTimeSpentForUserPerMonth(){
        viewModelProfileTimeSpentList.jobTimeSpentResult.observe(viewLifecycleOwner){
            timeSpentAdapter.setupData(it.collection.toList())
        }
        when(arguments?.getString("username") == null){
            true -> viewModelProfileTimeSpentList.getAllTimeSpentForUserPerMonth(SessionManager.getCurrentUserUsername(requireContext())!!)
            false -> viewModelProfileTimeSpentList.getAllTimeSpentForUserPerMonth(arguments?.getString("username")!!)
        }
    }

    private fun setObserverForError() {
        viewModelProfileTimeSpentList.errorResult.observe(viewLifecycleOwner){
            if(it == true) {
                ErrorDialogHandler(requireContext())
                viewModelProfileTimeSpentList.errorResult.value = false
            }
        }
    }

}