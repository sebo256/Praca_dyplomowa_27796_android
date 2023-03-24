package com.praca.dyplomowa.android.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.praca.dyplomowa.android.api.response.UserGetAllResponseCollection
import com.praca.dyplomowa.android.databinding.RecyclerJobApplyToLayoutBinding

class JobApplyToAdapter(private var data: UserGetAllResponseCollection, private var appliedUsers: Collection<String>): RecyclerView.Adapter<JobApplyToAdapter.ViewHolder>() {

    private var checkedUsers = ArrayList<String>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): JobApplyToAdapter.ViewHolder {
        val binding = RecyclerJobApplyToLayoutBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: JobApplyToAdapter.ViewHolder, position: Int) {
        viewHolder.binding.textViewRecyclerJobApplyToName.text = data.collection.elementAt(position).name
        viewHolder.binding.textViewRecyclerJobApplyToSurname.text = data.collection.elementAt(position).surname
        if(appliedUsers.contains(data.collection.elementAt(position).username)){
            viewHolder.binding.checkBoxRecyclerJobApplyTo.isChecked = true
        }
    }

    override fun getItemCount(): Int =
        data.collection.size

    inner class ViewHolder(val binding: RecyclerJobApplyToLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        init {
                binding.checkBoxRecyclerJobApplyTo.setOnCheckedChangeListener { compoundButton, isChecked ->
                    if(isChecked){
                        checkedUsers.add(data.collection.elementAt(adapterPosition).username)
                    }else {
                        checkedUsers.remove(data.collection.elementAt(adapterPosition).username)
                    }
                }
        }
    }

    fun getCheckedUsers(): Collection<String> =
        checkedUsers


}