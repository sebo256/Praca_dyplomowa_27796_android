package com.praca.dyplomowa.android.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.praca.dyplomowa.android.api.response.ClientGetAllResponse
import com.praca.dyplomowa.android.databinding.RecyclerClientsItemLayoutBinding
import com.praca.dyplomowa.android.databinding.RecyclerJobsItemLayoutBinding
import com.praca.dyplomowa.android.utils.RecyclerViewJobsUtilsInterface

class JobClientAdapter(
    private var recyclerViewJobsUtilsInterface: RecyclerViewJobsUtilsInterface
) : RecyclerView.Adapter<JobClientAdapter.ViewHolder>(), Filterable {

    private var actualFullList: List<ClientGetAllResponse> = mutableListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerClientsItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if(dataDiffer.currentList.elementAt(position).companyName.isNullOrBlank()){
            viewHolder.binding.textViewRecyclerClientCompanyNameNameSurname.text = dataDiffer.currentList.elementAt(position).name +
                    " " +
                    dataDiffer.currentList.elementAt(position).surname
        }else{
            viewHolder.binding.textViewRecyclerClientCompanyNameNameSurname.text = dataDiffer.currentList.elementAt(position).companyName
            viewHolder.binding.textViewRecyclerClientName.text = dataDiffer.currentList.elementAt(position).name
            viewHolder.binding.textViewRecyclerClientSurname.text = dataDiffer.currentList.elementAt(position).surname
        }

        viewHolder.binding.textViewRecyclerClientStreet.text = dataDiffer.currentList.elementAt(position).street
        viewHolder.binding.textViewRecyclerClientPostalCode.text = dataDiffer.currentList.elementAt(position).postalCode
        viewHolder.binding.textViewRecyclerClientCity.text = dataDiffer.currentList.elementAt(position).city

        viewHolder.binding.recyclerItemClient.setOnClickListener {
            recyclerViewJobsUtilsInterface.onClick(Gson().toJson(dataDiffer.currentList.elementAt(viewHolder.bindingAdapterPosition)))
        }
        viewHolder.binding.recyclerItemClient.setOnLongClickListener {
            recyclerViewJobsUtilsInterface.onLongClick(dataDiffer.currentList.elementAt(viewHolder.bindingAdapterPosition).id)
            true
        }

    }

    override fun getFilter(): Filter = searchingFilter

    private val searchingFilter = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filteringList = mutableListOf<ClientGetAllResponse>()
            if(p0.isNullOrEmpty()){
                filteringList.addAll(actualFullList)
            } else {
                val charsToFind = p0.toString().lowercase().trim()
                actualFullList.forEach {
                    if(it.companyName!!.lowercase().contains(charsToFind) ||
                        it.name.lowercase().contains(charsToFind) ||
                        it.surname.lowercase().contains(charsToFind) ){
                        filteringList.add(it)
                    }
                }

            }
            val searchingResult = FilterResults()
            searchingResult.values = filteringList
            return searchingResult
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            dataDiffer.submitList(p1?.values as MutableList<ClientGetAllResponse>)
        }
    }

    override fun getItemCount() =
        dataDiffer.currentList.size

    inner class ViewHolder(val binding: RecyclerClientsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    fun setupData(data: List<ClientGetAllResponse>) {
        actualFullList = data.toMutableList()
        dataDiffer.submitList(data)
    }

    private val diffUtil = object : DiffUtil.ItemCallback<ClientGetAllResponse>() {
        override fun areItemsTheSame(
            oldItem: ClientGetAllResponse,
            newItem: ClientGetAllResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ClientGetAllResponse,
            newItem: ClientGetAllResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

    val dataDiffer = AsyncListDiffer(this, diffUtil)

}