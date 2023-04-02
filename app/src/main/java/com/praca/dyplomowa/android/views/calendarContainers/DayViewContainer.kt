package com.praca.dyplomowa.android.views.calendarContainers

import android.view.View
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.ViewContainer
import com.praca.dyplomowa.android.databinding.CalendarDaytileLayoutBinding
import com.praca.dyplomowa.android.utils.CalendarViewContainersUtilsInterface
import java.time.LocalDateTime
import java.time.ZonedDateTime

class DayViewContainer(view: View, private var calendarViewContainersUtilsInterface: CalendarViewContainersUtilsInterface) : ViewContainer(view) {
    val textView = CalendarDaytileLayoutBinding.bind(view).calendarDayTileText
    val jobDot = CalendarDaytileLayoutBinding.bind(view).eventDotDayTile
    lateinit var dates: CalendarDay
    init {
        view.setOnClickListener{
            calendarViewContainersUtilsInterface.onClick(
                LocalDateTime.of(dates.date.year, dates.date.month, dates.date.dayOfMonth, 0, 0).toInstant(ZonedDateTime.now().offset).toEpochMilli() -1,
                LocalDateTime.of(dates.date.year, dates.date.month, dates.date.dayOfMonth, 23, 59).toInstant(ZonedDateTime.now().offset).toEpochMilli() +1
            )
        }
    }
}