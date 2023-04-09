package com.praca.dyplomowa.android.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.response.JobGetForListResponse
import com.praca.dyplomowa.android.api.response.UserGetAllResponse
import com.praca.dyplomowa.android.databinding.RecyclerJobsItemLayoutBinding
import com.praca.dyplomowa.android.databinding.RecyclerProfileUsersListLayoutBinding
import com.praca.dyplomowa.android.utils.RecyclerViewUtilsInterface

class ProfileUsersListAdapter(
    private var recyclerViewUtilsInterface: RecyclerViewUtilsInterface
): RecyclerView.Adapter<ProfileUsersListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerProfileUsersListLayoutBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.binding.textViewRecyclerProfileUsersListName.text = dataDiffer.currentList.elementAt(position).name
            viewHolder.binding.textViewRecyclerProfileUsersListSurname.text = dataDiffer.currentList.elementAt(position).surname

        viewHolder.binding.recyclerProfileUsersListItem.setOnClickListener {
            recyclerViewUtilsInterface.onClick(dataDiffer.currentList.elementAt(viewHolder.bindingAdapterPosition).username)
        }
        viewHolder.binding.recyclerProfileUsersListItem.setOnLongClickListener {
            recyclerViewUtilsInterface.onLongClick(dataDiffer.currentList.elementAt(viewHolder.bindingAdapterPosition).username)
            true
        }
    }

    override fun getItemCount() =
        dataDiffer.currentList.size

    inner class ViewHolder(val binding: RecyclerProfileUsersListLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.recyclerProfileUsersListItem.setOnClickListener{

                println(dataDiffer.currentList.elementAt(bindingAdapterPosition).id)
            }
        }
    }

    fun setupData(data: List<UserGetAllResponse>) =
        dataDiffer.submitList(data)

    private val diffUtil = object : DiffUtil.ItemCallback<UserGetAllResponse>() {
        override fun areItemsTheSame(
            oldItem: UserGetAllResponse,
            newItem: UserGetAllResponse
        ): Boolean {
            return oldItem.id == newItem.id
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