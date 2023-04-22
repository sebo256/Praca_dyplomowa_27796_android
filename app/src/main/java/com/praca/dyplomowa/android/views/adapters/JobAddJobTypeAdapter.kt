package com.praca.dyplomowa.android.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.praca.dyplomowa.android.api.response.JobTypeGetAllResponse
import com.praca.dyplomowa.android.databinding.DropdownJobTypesLayoutBinding
import com.praca.dyplomowa.android.utils.RecyclerViewJobsUtilsInterface

class JobAddJobTypeAdapter(
    private val context: Context,
    private val layout: Int,
    private val list: MutableList<JobTypeGetAllResponse>,
    private var recyclerViewJobsUtilsInterface: RecyclerViewJobsUtilsInterface
) :ArrayAdapter<JobTypeGetAllResponse>(context, layout, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: DropdownJobTypesLayoutBinding
        when(convertView == null){
            true -> binding = DropdownJobTypesLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            false -> binding = DropdownJobTypesLayoutBinding.bind(convertView)
        }
        binding.textViewJobTypeAdapter.text = list.elementAt(position).jobType
        binding.textViewJobTypeAdapter.setOnClickListener {
            recyclerViewJobsUtilsInterface.onClick(list.elementAt(position).jobType,list.elementAt(position).id)
        }
        return binding.root
    }

}