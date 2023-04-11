package com.praca.dyplomowa.android.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.praca.dyplomowa.android.api.response.JobTimeSpentResponse
import com.praca.dyplomowa.android.databinding.RecyclerProfileTimeSpentLayoutBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.TextStyle
import java.util.*

class ProfileTimeSpentAdapter() : RecyclerView.Adapter<ProfileTimeSpentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerProfileTimeSpentLayoutBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewRecyclerProfileTimeSpentName.text = dateStringToLocalizedStringDate(dataDiffer.currentList.elementAt(position).name)
        viewHolder.binding.textViewRecyclerProfileTimeSpentNumber.text = dataDiffer.currentList.elementAt(position).timeSpent.toString() + "h"

    }

    override fun getItemCount() =
        dataDiffer.currentList.size

    inner class ViewHolder(val binding: RecyclerProfileTimeSpentLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    private fun dateStringToLocalizedStringDate(dateString: String): String =
        englishStringDateToLocalDate(dateString).month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()).replaceFirstChar { it.uppercase() } +
                " " +
                englishStringDateToLocalDate(dateString).year

    private fun englishStringDateToLocalDate(dateString: String): LocalDate =
        YearMonth.parse(dateString, DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ofPattern("MMMM uuuu"))
            .toFormatter(Locale.ENGLISH)).atDay(1)


    fun setupData(data: List<JobTimeSpentResponse>) =
        dataDiffer.submitList(data)

    private val diffUtil = object : DiffUtil.ItemCallback<JobTimeSpentResponse>() {
        override fun areItemsTheSame(
            oldItem: JobTimeSpentResponse,
            newItem: JobTimeSpentResponse
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: JobTimeSpentResponse,
            newItem: JobTimeSpentResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

    val dataDiffer = AsyncListDiffer(this, diffUtil)

}