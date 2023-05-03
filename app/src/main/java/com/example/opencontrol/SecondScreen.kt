package com.example.opencontrol

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.ceil

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SecondScreen() {
    val selectedDate = remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        MyCalendarView(LocalDate.now()) { println("$it") }
//        val calendar = Calendar.getInstance()
//        AndroidView(modifier = Modifier.fillMaxWidth(), factory = { CalendarView(it) }, update = {
//            it.firstDayOfWeek = Calendar.MONDAY
//            it.setOnDateChangeListener { calendarView, year, mounth, day ->
//                selectedDate.value = "$day.${mounth+1}.$year"
//            }
//        })
//        Text(text = selectedDate.value)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyCalendarView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var monthOffset by remember { mutableStateOf(0) }
    val currentMonth = YearMonth.now().plusMonths(monthOffset.toLong())
    val monthTitle = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val yearTitle = currentMonth.year.toString()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Month selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { monthOffset-- }) {
                Icon(Icons.Filled.KeyboardArrowLeft, null)
            }

            Text(
                text = "$monthTitle $yearTitle",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = { monthOffset++ }) {
                Icon(Icons.Filled.KeyboardArrowRight, null)
            }
        }

        // Weekday header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val weekdays = WeekDays.values()
            weekdays.forEach {
                Text(
                    text = it.shortName,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Days grid
        val daysInMonth = currentMonth.lengthOfMonth()
        val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7
        val totalDays = firstDayOfMonth + daysInMonth
        val rows = ceil(totalDays.toFloat() / 7).toInt()

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(rows) { rowIndex ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (columnIndex in 0 until 7) {
                        val dayIndex = rowIndex * 7 + columnIndex - firstDayOfMonth+2
                        if (dayIndex in 1..daysInMonth) {
                            val date = currentMonth.atDay(dayIndex)
//                            Box(modifier = Modifier.weight(1f),
//                                contentAlignment = Alignment.Center) {
                                DayView(
                                    date = date,
                                    selectedDate = selectedDate,
                                    onDateSelected = onDateSelected
                                )
//                            }
                        } else {
                            Spacer(modifier = Modifier.width(48.dp))
                        }
                    }
                }
            }
        }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayView(
    date: LocalDate,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val isSelected = date == selectedDate
    val isToday = date == LocalDate.now()
    val isFutureDate = date > LocalDate.now()

    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val cellWidth = screenWidthDp / 7

    val layoutDirection = LocalLayoutDirection.current
    val density = LocalDensity.current
//    val parentWidth = with(density) {
//        constraints.maxWidth.toDp().value / layoutDirection.resolveLayoutDirection().sign
//    }

        Box(
            modifier = Modifier
                .width(cellWidth)
                .height(cellWidth)
                .clip(CircleShape)
                .background(
                    when {
                        isSelected -> MaterialTheme.colors.secondary
                        isToday -> MaterialTheme.colors.primary.copy(alpha = 0.4f)
                        else -> MaterialTheme.colors.secondary
//                        else -> Color.Transparent
                    }
                )
                .then(
                    if (isFutureDate) Modifier.clickable(enabled = false) { }
                    else Modifier.clickable { onDateSelected(date) }
                )
//                .padding(8.dp)
//                .aspectRatio(1f)
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                color = when {
                    isSelected -> MaterialTheme.colors.onSecondary
                    isToday -> MaterialTheme.colors.primary
                    else -> MaterialTheme.colors.onSurface
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
}

enum class WeekDays(val shortName: String) {
    MONDAY("ПН"),
    TUESDAY("ВТ"),
    WEDNESDAY("СР"),
    THURSDAY("ЧТ"),
    FRIDAY("ПТ"),
    SATURDAY("СБ"),
    SUNDAY("ВС")
}
