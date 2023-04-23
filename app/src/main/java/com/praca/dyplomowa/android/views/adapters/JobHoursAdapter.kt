package com.praca.dyplomowa.android.views.adapters

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.response.JobGetForListHoursResponse
import com.praca.dyplomowa.android.databinding.RecyclerProfileJobsHoursLayoutBinding
import com.praca.dyplomowa.android.utils.RecyclerViewJobsUtilsInterface

class JobHoursAdapter(
    private var recyclerViewJobsUtilsInterface: RecyclerViewJobsUtilsInterface
) : RecyclerView.Adapter<JobHoursAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerProfileJobsHoursLayoutBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewRecyclerJobHoursSubject.text = dataDiffer.currentList.elementAt(position).subject
        viewHolder.binding.textViewRecyclerJobHoursType.text = dataDiffer.currentList.elementAt(position).jobType
        viewHolder.binding.textViewRecyclerJobHoursCompanyName.text = dataDiffer.currentList.elementAt(position).companyName
        viewHolder.binding.textViewRecyclerJobHoursName.text = dataDiffer.currentList.elementAt(position).name
        viewHolder.binding.textViewRecyclerJobHoursSurname.text = dataDiffer.currentList.elementAt(position).surname
        viewHolder.binding.textViewRecyclerJobHoursCity.text = dataDiffer.currentList.elementAt(position).city
        viewHolder.binding.textViewRecyclerJobHoursStreet.text = dataDiffer.currentList.elementAt(position).street
        viewHolder.binding.textViewRecyclerJobHoursTimeSpent.text = dataDiffer.currentList.elementAt(position).timeSpent.toString() + "h"


        viewHolder.binding.recyclerItemProfileJobHours.setOnClickListener {
            recyclerViewJobsUtilsInterface.onClick(dataDiffer.currentList.elementAt(viewHolder.bindingAdapterPosition).id)
        }
        viewHolder.binding.recyclerItemProfileJobHours.setOnLongClickListener {
            recyclerViewJobsUtilsInterface.onLongClick(dataDiffer.currentList.elementAt(viewHolder.bindingAdapterPosition).id)
            true
        }


        if(viewHolder.binding.root.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES){
            when(dataDiffer.currentList.elementAt(position).isCompleted){
                true -> viewHolder.binding.recyclerItemProfileJobHours.setCardBackgroundColor(ContextCompat.getColor(viewHolder.binding.root.context, R.color.green_700))
                false -> viewHolder.binding.recyclerItemProfileJobHours.setCardBackgroundColor(ContextCompat.getColor(viewHolder.binding.root.context, R.color.red_700))
            }
        }else{
            when(dataDiffer.currentList.elementAt(position).isCompleted){
                true -> viewHolder.binding.recyclerItemProfileJobHours.setCardBackgroundColor(ContextCompat.getColor(viewHolder.binding.root.context, R.color.green_200))
                false -> viewHolder.binding.recyclerItemProfileJobHours.setCardBackgroundColor(ContextCompat.getColor(viewHolder.binding.root.context, R.color.red_200))
            }
        }

    }

    override fun getItemCount() =
        dataDiffer.currentList.size

    inner class ViewHolder(val binding: RecyclerProfileJobsHoursLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    fun setupData(data: List<JobGetForListHoursResponse>) {
        dataDiffer.submitList(data)
    }

    private val diffUtil = object : DiffUtil.ItemCallback<JobGetForListHoursResponse>() {
        override fun areItemsTheSame(
            oldItem: JobGetForListHoursResponse,
            newItem: JobGetForListHoursResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: JobGetForListHoursResponse,
            newItem: JobGetForListHoursResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

    val dataDiffer = AsyncListDiffer(this, diffUtil)

}