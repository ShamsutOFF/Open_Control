package com.example.opencontrol.noteTab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opencontrol.ui.theme.Invisible
import com.example.opencontrol.ui.theme.LightColors
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyCalendarView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var monthOffset by remember { mutableStateOf(0) }
    val currentMonth = YearMonth.now().plusMonths(monthOffset.toLong())
    val monthTitle = currentMonth.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
    val yearTitle = currentMonth.year.toString()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        MonthSelector(monthOffset, monthTitle, yearTitle) { monthOffset = it }
        WeekdayHeader()
        DaysGrid(currentMonth, selectedDate, onDateSelected)
    }
}

@Composable
private fun MonthSelector(
    monthOffset: Int,
    monthTitle: String,
    yearTitle: String,
    function: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { function(monthOffset - 1) }, modifier = Modifier.weight(1f)) {
            Icon(Icons.Filled.KeyboardArrowLeft, null)
        }

        Text(
            text = "$monthTitle $yearTitle",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = { function(monthOffset + 1) }, modifier = Modifier.weight(1f)) {
            Icon(Icons.Filled.KeyboardArrowRight, null)
        }
    }
}

@Composable
private fun WeekdayHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val weekdays = WeekDays.values()
        weekdays.forEach {
            Text(
                text = it.shortName,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DaysGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7
    val firstEmptyCells = if (firstDayOfMonth - 1 < 0) 6 else firstDayOfMonth - 1
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(firstEmptyCells + daysInMonth) { dayIndex ->
            if (dayIndex < firstEmptyCells) {
                val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
                val cellSize = screenWidthDp / 7
                Spacer(modifier = Modifier.width(cellSize))
            } else {
                val date = currentMonth.atDay(dayIndex - firstEmptyCells + 1)
                DayView(
                    date = date,
                    selectedDate = selectedDate,
                    onDateSelected = onDateSelected
                )
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

    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val cellWidth = screenWidthDp / 7

    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(cellWidth)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> LightColors.primary
//                    isToday -> LightColors.primary.copy(alpha = 0.4f)
                    else -> Color.Transparent
                }
            )
            .border(
                width = when {
                    isToday -> 2.dp
                    else -> 0.dp
                }, color = when {
                    isToday -> LightColors.primary
                    else -> Invisible
                }, shape = CircleShape
            )
            .clickable { onDateSelected(date) }
            .aspectRatio(1f)
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = when {
                isSelected -> LightColors.onPrimary
                isToday -> MaterialTheme.colors.primary
                else -> MaterialTheme.colors.onSurface
            },
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
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