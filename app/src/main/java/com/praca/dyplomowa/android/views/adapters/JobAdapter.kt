package com.praca.dyplomowa.android.views.adapters

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.response.JobGetForListResponse
import com.praca.dyplomowa.android.databinding.RecyclerJobsItemLayoutBinding
import com.praca.dyplomowa.android.utils.RecyclerViewJobsUtilsInterface

class JobAdapter(
    private var recyclerViewJobsUtilsInterface: RecyclerViewJobsUtilsInterface
) : RecyclerView.Adapter<JobAdapter.ViewHolder>(), Filterable {

    private var actualFullList: List<JobGetForListResponse> = mutableListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerJobsItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewRecyclerJobSubject.text = dataDiffer.currentList.elementAt(position).subject
        viewHolder.binding.textViewRecyclerJobType.text = dataDiffer.currentList.elementAt(position).jobType
        viewHolder.binding.textViewRecyclerJobCompanyName.text = dataDiffer.currentList.elementAt(position).companyName
        viewHolder.binding.textViewRecyclerJobName.text = dataDiffer.currentList.elementAt(position).name
        viewHolder.binding.textViewRecyclerJobSurname.text = dataDiffer.currentList.elementAt(position).surname
        viewHolder.binding.textViewRecyclerJobCity.text = dataDiffer.currentList.elementAt(position).city
        viewHolder.binding.textViewRecyclerJobStreet.text = dataDiffer.currentList.elementAt(position).street


        viewHolder.binding.recyclerItem.setOnClickListener {
            recyclerViewJobsUtilsInterface.onClick(dataDiffer.currentList.elementAt(viewHolder.bindingAdapterPosition).id)
        }
        viewHolder.binding.recyclerItem.setOnLongClickListener {
            recyclerViewJobsUtilsInterface.onLongClick(dataDiffer.currentList.elementAt(viewHolder.bindingAdapterPosition).id)
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

    override fun getFilter(): Filter = searchingFilter

    private val searchingFilter = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filteringList = mutableListOf<JobGetForListResponse>()
            if(p0.isNullOrEmpty()){
                filteringList.addAll(actualFullList)
            } else {
                val charsToFind = p0.toString().lowercase().trim()
                actualFullList.forEach {
                    if(it.subject.lowercase().contains(charsToFind) ||
                        it.companyName!!.lowercase().contains(charsToFind) ||
                        it.name.lowercase().contains(charsToFind) ||
                        it.surname.lowercase().contains(charsToFind)){
                        filteringList.add(it)
                    }
                }

            }
            val searchingResult = FilterResults()
            searchingResult.values = filteringList
            return searchingResult
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            dataDiffer.submitList(p1?.values as MutableList<JobGetForListResponse>)
        }
    }

    override fun getItemCount() =
        dataDiffer.currentList.size

    inner class ViewHolder(val binding: RecyclerJobsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    fun setupData(data: List<JobGetForListResponse>) {
        actualFullList = data.toMutableList()
        dataDiffer.submitList(data)
    }

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