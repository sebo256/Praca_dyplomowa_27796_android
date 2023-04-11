package com.praca.dyplomowa.android.views.adapters

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.response.JobGetForListResponse
import com.praca.dyplomowa.android.databinding.RecyclerJobsItemLayoutBinding
import com.praca.dyplomowa.android.utils.RecyclerViewUtilsInterface

class JobAdapter(
    private var recyclerViewUtilsInterface: RecyclerViewUtilsInterface
) : RecyclerView.Adapter<JobAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerJobsItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewRecyclerJobSubject.text = dataDiffer.currentList.elementAt(position).subject
        viewHolder.binding.textViewRecyclerJobCompanyName.text = dataDiffer.currentList.elementAt(position).companyName
        viewHolder.binding.textViewRecyclerJobName.text = dataDiffer.currentList.elementAt(position).name
        viewHolder.binding.textViewRecyclerJobSurname.text = dataDiffer.currentList.elementAt(position).surname
        viewHolder.binding.textViewRecyclerJobCity.text = dataDiffer.currentList.elementAt(position).city
        viewHolder.binding.textViewRecyclerJobStreet.text = dataDiffer.currentList.elementAt(position).street


        viewHolder.binding.recyclerItem.setOnClickListener {
            recyclerViewUtilsInterface.onClick(dataDiffer.currentList.elementAt(viewHolder.bindingAdapterPosition).id)
        }
        viewHolder.binding.recyclerItem.setOnLongClickListener {
            recyclerViewUtilsInterface.onLongClick(dataDiffer.currentList.elementAt(viewHolder.bindingAdapterPosition).id)
            true
        }


        if(viewHolder.binding.root.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES){
            when(dataDiffer.currentList.elementAt(position).isCompleted){
                true -> viewHolder.binding.recyclerItem.setCardBackgroundColor(ContextCompat.getColor(viewHolder.binding.root.context, R.color.green_700))
                false -> viewHolder.binding.recyclerItem.setCardBackgroundColor(ContextCompat.getColor(viewHolder.binding.root.context, R.color.red_700))
            }
        }else{
            when(dataDiffer.currentList.elementAt(position).isCompleted){
                true -> viewHolder.binding.recyclerItem.setCardBackgroundColor(ContextCompat.getColor(viewHolder.binding.root.context, R.color.green_200))
                false -> viewHolder.binding.recyclerItem.setCardBackgroundColor(ContextCompat.getColor(viewHolder.binding.root.context, R.color.red_200))
            }
        }

    }

    override fun getItemCount() =
        dataDiffer.currentList.size

    inner class ViewHolder(val binding: RecyclerJobsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    fun setupData(data: List<JobGetForListResponse>) =
        dataDiffer.submitList(data)

    private val diffUtil = object : DiffUtil.ItemCallback<JobGetForListResponse>() {
        override fun areItemsTheSame(
            oldItem: JobGetForListResponse,
            newItem: JobGetForListResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: JobGetForListResponse,
            newItem: JobGetForListResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

    val dataDiffer = AsyncListDiffer(this, diffUtil)

}