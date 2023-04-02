package com.praca.dyplomowa.android.views.calendarContainers

import android.view.View
import com.kizitonwose.calendar.view.ViewContainer
import com.praca.dyplomowa.android.databinding.CalendarDayTitlesContainersBinding

class MonthViewContainer(view: View) : ViewContainer(view) {
    val titlesContainer = CalendarDayTitlesContainersBinding.bind(view).daysTitlesContainersLayout
}
