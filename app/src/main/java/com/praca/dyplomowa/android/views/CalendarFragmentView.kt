package com.praca.dyplomowa.android.views

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.kizitonwose.calendar.core.*
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.praca.dyplomowa.android.api.response.JobGetDatesAndInfoResponse
import com.praca.dyplomowa.android.databinding.FragmentCalendarViewBinding
import com.praca.dyplomowa.android.utils.CalendarViewContainersUtilsInterface
import com.praca.dyplomowa.android.utils.DateRange
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.viewmodels.CalendarViewModel
import com.praca.dyplomowa.android.views.calendarContainers.DayViewContainer
import com.praca.dyplomowa.android.views.calendarContainers.MonthViewContainer
import java.time.DayOfWeek
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*


class CalendarFragmentView : Fragment() {
    private var _binding: FragmentCalendarViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelCalendar: CalendarViewModel
    var jobDatesAndInfoList: MutableList<JobGetDatesAndInfoResponse> = mutableListOf()
    var currentMonth = YearMonth.now()
    val startMonth = currentMonth.minusMonths(60)
    val endMonth = currentMonth.plusMonths(60)
    val firstDayOfWeek = firstDayOfWeekFromLocale()
    val daysOfWeek: List<DayOfWeek> = daysOfWeek(firstDayOfWeek)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentCalendarViewBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModelCalendar = ViewModelProvider(requireActivity()).get(CalendarViewModel::class.java)
        setObserverForGetJobGetDatesAndInfoResponse()
        setObserverForError()
//        setObserverForGetJobByLongDateBetween()

        return view
    }

    fun checkIfDarkModeIsOn() =
        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    fun setObserverForGetJobGetDatesAndInfoResponse(){
        viewModelCalendar.jobDatesAndInfoResult.observe(viewLifecycleOwner){
            jobDatesAndInfoList = it.collection.toMutableList()
            setupCalendar(binding)
        }
        viewModelCalendar.getJobDatesAndInfo()
    }

    private fun setObserverForError() {
        viewModelCalendar.errorResult.observe(viewLifecycleOwner){
            if(it == true) {
                ErrorDialogHandler(requireContext())
                viewModelCalendar.errorResult.value = false
            }
        }
    }


    private val calendarViewContainersUtilsInterface: CalendarViewContainersUtilsInterface = object : CalendarViewContainersUtilsInterface {
        override fun onClick(startLong: Long, endLong: Long) {
            val intent = Intent(requireContext(), CalendarJobListView::class.java)
            intent.putExtra("dateRange", Gson().toJson(DateRange(startLong = startLong, endLong = endLong)))
            startActivity(intent)
        }
    }

    fun setupCalendar(binding: FragmentCalendarViewBinding){
        binding.calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        binding.calendarView.scrollToMonth(currentMonth)
        setupCalendarDayBinder(binding)
        setupCalendarMonthBinder(binding)


        binding.calendarView.monthScrollListener = {
            val date = binding.calendarView.findFirstVisibleMonth()?.yearMonth
            currentMonth = binding.calendarView.findFirstVisibleMonth()?.yearMonth
            binding.textViewYearCalendarFragment.text = date!!.month.getDisplayName(TextStyle.FULL_STANDALONE, resources.configuration.locales.get(0)) +
                    " " +
                    date.year.toString()

        }

    }

    private fun setupCalendarDayBinder(binding: FragmentCalendarViewBinding) {
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {

            override fun create(view: View) = DayViewContainer(view, calendarViewContainersUtilsInterface)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.dates = data
                container.textView.text = data.date.dayOfMonth.toString()
                container.jobDot.visibility = View.INVISIBLE
                jobDatesAndInfoList.forEach {
                    if(data.date == Instant.ofEpochMilli(it.plannedDate!!).atZone(ZoneId.systemDefault()).toLocalDate()){
                        container.jobDot.visibility = View.VISIBLE
                    }
                }

                if(checkIfDarkModeIsOn()){
                    when(data.position == DayPosition.MonthDate){
                        true -> container.textView.setTextColor(Color.WHITE)
                        false -> container.textView.setTextColor(Color.GRAY)
                    }
                }else{
                    when(data.position == DayPosition.MonthDate){
                        true -> container.textView.setTextColor(Color.BLACK)
                        false -> container.textView.setTextColor(Color.GRAY)
                    }
                }

            }
        }
    }

    private fun setupCalendarMonthBinder(binding: FragmentCalendarViewBinding) {
        binding.calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer>{
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                if(container.titlesContainer.tag == null) {
                    container.titlesContainer.tag = data.yearMonth
                    container.titlesContainer.children.map { it as TextView }
                        .forEachIndexed { index, textView ->
                            val dayOfWeek = daysOfWeek.elementAt(index)
                            val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            textView.text = title
                        }

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModelCalendar.getJobDatesAndInfo()
        setupCalendar(binding)
    }




}

