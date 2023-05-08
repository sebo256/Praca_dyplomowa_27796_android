package com.praca.dyplomowa.android.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.praca.dyplomowa.android.api.response.JobAppliedToResponse
import com.praca.dyplomowa.android.api.response.UserGetAllResponse
import com.praca.dyplomowa.android.databinding.RecyclerJobApplyToLayoutBinding
import com.praca.dyplomowa.android.utils.SessionManager

class JobApplyToAdapter(
    private var jobAppliedToResponse: JobAppliedToResponse
): RecyclerView.Adapter<JobApplyToAdapter.ViewHolder>() {

    private var checkedUsers = ArrayList<String>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): JobApplyToAdapter.ViewHolder {
        val binding = RecyclerJobApplyToLayoutBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: JobApplyToAdapter.ViewHolder, position: Int) {
        viewHolder.binding.textViewRecyclerJobApplyToName.text = dataDiffer.currentList.elementAt(position).name
        viewHolder.binding.textViewRecyclerJobApplyToSurname.text = dataDiffer.currentList.elementAt(position).surname

        if(jobAppliedToResponse.jobAppliedTo.contains(dataDiffer.currentList.elementAt(position).username)){
            viewHolder.binding.checkBoxRecyclerJobApplyTo.isChecked = true
            viewHolder.binding.textFieldTextTimeJobApplyToFragment.setText(jobAppliedToResponse.timeSpent.get(dataDiffer.currentList.elementAt(position).username).toString())
        }
    }

    override fun getItemCount(): Int =
        dataDiffer.currentList.size

    inner class ViewHolder(val binding: RecyclerJobApplyToLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.checkBoxRecyclerJobApplyTo.setOnCheckedChangeListener { compoundButton, isChecked ->
                if(isChecked){
                    checkedUsers.add(dataDiffer.currentList.elementAt(bindingAdapterPosition).username)
                    binding.textFieldLayoutTimeJobApplyToFragment.visibility = View.VISIBLE
                }else {
                    checkedUsers.remove(dataDiffer.currentList.elementAt(bindingAdapterPosition).username)
                    binding.textFieldLayoutTimeJobApplyToFragment.visibility = View.INVISIBLE
                }
            }
        }
    }


    fun getCheckedUsers(): Collection<String> =
        checkedUsers

    fun setupData(data: List<UserGetAllResponse>) =
        dataDiffer.submitList(data)

    private val diffUtil = object : DiffUtil.ItemCallback<UserGetAllResponse>() {
        override fun areItemsTheSame(
            oldItem: UserGetAllResponse,
            newItem: UserGetAllResponse
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: UserGetAllResponse,
            newItem: UserGetAllResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

    val dataDiffer = AsyncListDiffer(this, diffUtil)
}