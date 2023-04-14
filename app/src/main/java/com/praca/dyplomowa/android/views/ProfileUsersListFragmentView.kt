package com.praca.dyplomowa.android.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.praca.dyplomowa.android.databinding.FragmentProfileUsersListViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.utils.FragmentNavigationUtils
import com.praca.dyplomowa.android.utils.RecyclerViewUtilsInterface
import com.praca.dyplomowa.android.viewmodels.ProfileUsersListViewModel
import com.praca.dyplomowa.android.views.adapters.ProfileUsersListAdapter


class ProfileUsersListFragmentView : Fragment() {
    private var _binding: FragmentProfileUsersListViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelProfileUserList: ProfileUsersListViewModel
    private lateinit var usersAdapter: ProfileUsersListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileUsersListViewBinding.inflate(inflater, container, false)


        viewModelProfileUserList = ViewModelProvider(requireActivity()).get(ProfileUsersListViewModel::class.java)
        setObserverForGetUsers()
        setObserverForError()

        binding.recyclewViewProfileUsersList.layoutManager = LinearLayoutManager(requireContext())
        usersAdapter = ProfileUsersListAdapter(recyclerViewUtilsInterface)
        binding.recyclewViewProfileUsersList.adapter = usersAdapter

        return binding.root
    }

    private fun setObserverForGetUsers(){
        viewModelProfileUserList.userReponse.observe(viewLifecycleOwner){
            usersAdapter.setupData(it.collection.toList())
        }
        viewModelProfileUserList.getUsers()
    }

    private fun setObserverForError() {
        viewModelProfileUserList.errorResult.observe(viewLifecycleOwner){
            if(it == true) {
                ErrorDialogHandler(requireContext())
                viewModelProfileUserList.errorResult.value = false
            }
        }
    }

    private val recyclerViewUtilsInterface: RecyclerViewUtilsInterface = object :
        RecyclerViewUtilsInterface {
        override fun onClick(string: String) {
            FragmentNavigationUtils.addFragmentFadeWithOneStringBundleValue(
                fragmentManager = parentFragmentManager,
                fragment = ProfileTimeSpentListFragmentView(),
                argumentKey = "username",
                argumentValue = string
            )
        }

        override fun onLongClick(string: String) {
            FragmentNavigationUtils.addFragmentFadeWithOneStringBundleValue(
                fragmentManager = parentFragmentManager,
                fragment = ProfileTimeSpentListFragmentView(),
                argumentKey = "username",
                argumentValue = string
            )
        }
    }

}