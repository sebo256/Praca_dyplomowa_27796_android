package com.praca.dyplomowa.android.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.praca.dyplomowa.android.api.response.JobGetAllResponseCollection
import com.praca.dyplomowa.android.databinding.RecyclerJobsItemLayoutBinding
import com.praca.dyplomowa.android.utils.RecyclerViewUtilsInterface

class JobAdapter(
    private var data: ArrayList<JobGetAllResponseCollection>,
    private var recyclerViewUtilsInterface: RecyclerViewUtilsInterface
) :
    RecyclerView.Adapter<JobAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerJobsItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewRecyclerJobSubject.text = data.get(0).collection.elementAt(position).subject
        viewHolder.binding.textViewRecyclerJobCompanyName.text = data.get(0).collection.elementAt(position).companyName
        viewHolder.binding.textViewRecyclerJobName.text = data.get(0).collection.elementAt(position).name
        viewHolder.binding.textViewRecyclerJobSurname.text = data.get(0).collection.elementAt(position).name
        viewHolder.binding.textViewRecyclerJobCity.text = data.get(0).collection.elementAt(position).city
        viewHolder.binding.textViewRecyclerJobStreet.text = data.get(0).collection.elementAt(position).street

        viewHolder.binding.recyclerItem.setOnClickListener {
            recyclerViewUtilsInterface.onClick(data.get(0).collection.elementAt(position).id)
        }
    }

    override fun getItemCount() =
        data.get(0).collection.size


    inner class ViewHolder(val binding: RecyclerJobsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.recyclerItem.setOnClickListener{

                println(data.get(0).collection.elementAt(adapterPosition).id)
            }
        }
    }

}