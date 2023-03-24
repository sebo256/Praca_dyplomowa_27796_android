package com.praca.dyplomowa.android.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.praca.dyplomowa.android.api.response.JobGetAllResponse
import com.praca.dyplomowa.android.databinding.RecyclerJobsItemLayoutBinding
import com.praca.dyplomowa.android.utils.RecyclerViewUtilsInterface

class JobAdapter(
//    private var data: MutableList<JobGetAllResponse>,
    private var recyclerViewUtilsInterface: RecyclerViewUtilsInterface
) : RecyclerView.Adapter<JobAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerJobsItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//        viewHolder.binding.textViewRecyclerJobSubject.text = data.elementAt(position).subject
//        viewHolder.binding.textViewRecyclerJobCompanyName.text = data.elementAt(position).companyName
//        viewHolder.binding.textViewRecyclerJobName.text = data.elementAt(position).name
//        viewHolder.binding.textViewRecyclerJobSurname.text = data.elementAt(position).name
//        viewHolder.binding.textViewRecyclerJobCity.text = data.elementAt(position).city
//        viewHolder.binding.textViewRecyclerJobStreet.text = data.elementAt(position).street
//
//        viewHolder.binding.recyclerItem.setOnClickListener {
//            recyclerViewUtilsInterface.onClick(data.elementAt(position).id)
//        }

        viewHolder.binding.textViewRecyclerJobSubject.text = dataDiffer.currentList.elementAt(position).subject
        viewHolder.binding.textViewRecyclerJobCompanyName.text = dataDiffer.currentList.elementAt(position).companyName
        viewHolder.binding.textViewRecyclerJobName.text = dataDiffer.currentList.elementAt(position).name
        viewHolder.binding.textViewRecyclerJobSurname.text = dataDiffer.currentList.elementAt(position).surname
        viewHolder.binding.textViewRecyclerJobCity.text = dataDiffer.currentList.elementAt(position).city
        viewHolder.binding.textViewRecyclerJobStreet.text = dataDiffer.currentList.elementAt(position).street

        viewHolder.binding.recyclerItem.setOnClickListener {
            recyclerViewUtilsInterface.onClick(dataDiffer.currentList.elementAt(position).id)
        }

    }

    override fun getItemCount() =
        dataDiffer.currentList.size

    inner class ViewHolder(val binding: RecyclerJobsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.recyclerItem.setOnClickListener{

                println(dataDiffer.currentList.elementAt(adapterPosition).id)
            }
        }
    }

    fun setupData(data: MutableList<JobGetAllResponse>) =
        dataDiffer.submitList(data)

    private val diffUtil = object : DiffUtil.ItemCallback<JobGetAllResponse>() {
        override fun areItemsTheSame(
            oldItem: JobGetAllResponse,
            newItem: JobGetAllResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: JobGetAllResponse,
            newItem: JobGetAllResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
    val dataDiffer = AsyncListDiffer(this, diffUtil)
}