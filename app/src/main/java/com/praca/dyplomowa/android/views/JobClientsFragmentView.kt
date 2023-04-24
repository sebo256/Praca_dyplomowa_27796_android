package com.praca.dyplomowa.android.views

import android.content.Context
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.FragmentJobClientsViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.utils.FragmentNavigationUtils
import com.praca.dyplomowa.android.utils.RecyclerViewJobsUtilsInterface
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.JobClientsViewModel
import com.praca.dyplomowa.android.views.adapters.JobClientAdapter

class JobClientsFragmentView : Fragment() {

    private var _binding: FragmentJobClientsViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var jobClientAdapter: JobClientAdapter
    private lateinit var viewModelJobClients: JobClientsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken,0)
        view.requestFocus()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJobClientsViewBinding.inflate(inflater, container, false)

        binding.recyclerViewClient.layoutManager = LinearLayoutManager(requireContext())
        jobClientAdapter = JobClientAdapter(recyclerViewJobsUtilsInterface)
        binding.recyclerViewClient.adapter = jobClientAdapter

        viewModelJobClients = ViewModelProvider(this).get(JobClientsViewModel::class.java)
        setObserverForError()
        setObserverForGetClients()
        setObserverForScrollingToNewItem()
        setObserverForDeleteClient()
        getClients()

        binding.buttonAddClientClientFragment.setOnClickListener {
            FragmentNavigationUtils.addFragmentFadeWithSourceFragment(
                fragmentManager = parentFragmentManager,
                fragment = JobClientAddFragmentView(),
                argumentSourceFragmentName = "JobClientsFragmentView"
            )
        }

        binding.textFieldTextSearchClientClientFragment.setOnEditorActionListener { textView, actionId, keyEvent ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                jobClientAdapter.filter.filter(binding.textFieldTextSearchClientClientFragment.text)
            }
            true
        }

        binding.buttonSearchClientClientFragment.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.root)
            when(binding.textFieldLayoutSearchClientClientFragment.visibility == View.GONE){
                true -> binding.textFieldLayoutSearchClientClientFragment.visibility = View.VISIBLE
                false -> hideSearchBarAndShowFullList()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                TransitionManager.beginDelayedTransition(binding.root)
                if(parentFragmentManager.backStackEntryCount == 2){
                    when(binding.textFieldLayoutSearchClientClientFragment.visibility == View.VISIBLE){
                        true -> hideSearchBarAndShowFullList()
                        false -> parentFragmentManager.popBackStack()
                    }
                }else{
                    parentFragmentManager.popBackStack()
                }
            }
        })

        return binding.root
    }

    private fun setObserverForGetClients() {
        viewModelJobClients.clientResult.observe(viewLifecycleOwner){
            jobClientAdapter.setupData(it.collection.toList())
        }
    }

    private fun setObserverForError() {
        viewModelJobClients.errorResult.observe(viewLifecycleOwner){
            if(it == true) {
                ErrorDialogHandler(requireContext())
                viewModelJobClients.errorResult.value = false
            }
        }
    }

    private fun setObserverForDeleteClient() {
        viewModelJobClients.deleteResult.observe(viewLifecycleOwner){
            if(it.status == true || it == null) {
                getClients()
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.dialog_error_text_title)
                    .setMessage(R.string.dialog_delete_error_text_message)
                    .setPositiveButton(R.string.dialog_error_positive_title) { dialog, which ->
                    }
                    .setOnDismissListener {  }
                    .show()
            }
        }
    }

    private fun setObserverForScrollingToNewItem() {
        jobClientAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionToScroll: Int, count: Int) {
                binding.recyclerViewClient.smoothScrollToPosition(positionToScroll)
            }
        })
    }

    private fun hideSearchBarAndShowFullList(){
        jobClientAdapter.filter.filter("")
        binding.textFieldTextSearchClientClientFragment.setText("")
        binding.textFieldLayoutSearchClientClientFragment.visibility = View.GONE
    }

    fun getClients() =
        viewModelJobClients.getClients()

    private val recyclerViewJobsUtilsInterface: RecyclerViewJobsUtilsInterface = object :
        RecyclerViewJobsUtilsInterface {
        override fun onClick(string: String) {
            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(requireView().windowToken,0)
            requireView().requestFocus()
            parentFragmentManager.setFragmentResult("pickedClientResult", bundleOf("pickedClient" to string))
            parentFragmentManager.popBackStack()
        }

        override fun onLongClick(string: String) {
            val materialDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.dialog_joblist_text_title)
                .setMessage(R.string.dialog_clients_text_message)
                .setPositiveButton(R.string.dialog_clients_positive_title) { dialog, which ->
                    FragmentNavigationUtils.addFragmentFadeWithOneStringBundleValueAndSourceFragment(
                        fragmentManager = parentFragmentManager,
                        fragment = JobClientAddFragmentView(),
                        argumentKey = "clientObjectId",
                        argumentValue = string,
                        argumentSourceFragmentName = "JobClientsFragmentView"
                    )
                }
                .setNeutralButton(R.string.dialog_clients_neutral_title) { dialog, which ->
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_clients_negative_title) { dialog, which ->
                    viewModelJobClients.deleteClients(string)
                }

            if(!SessionManager.getIsAdmin(requireContext())) {
                materialDialog.setNegativeButton("") { dialog, which -> }
            }
            materialDialog.show()
        }
    }

}