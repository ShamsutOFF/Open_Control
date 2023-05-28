package com.example.opencontrol.view.noteTab

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opencontrol.ui.theme.LightColors
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeeklyCalendar(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    var innerSelectedDate by remember {
        mutableStateOf(selectedDate)
    }
    var weekOffset by remember { mutableStateOf(0) }
    val dayOfWeek = innerSelectedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    val formattedDate = innerSelectedDate.format(DateTimeFormatter.ofPattern("yy.MM.dd"))

    val pagerState = rememberPagerState(
        initialPage = weekOffset + Int.MAX_VALUE / 2
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .map { it - Int.MAX_VALUE / 2 }
            .collect { weekOffset = it }
    }

    WeekSelector(weekOffset) { offset ->
        weekOffset = offset
    }

    HorizontalPager(
        pageCount = Int.MAX_VALUE,
        beyondBoundsPageCount = 1,
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        val calculatedWeekOffset = page - Int.MAX_VALUE / 2
        val startOfWeek = LocalDate.now().plusWeeks(calculatedWeekOffset.toLong())
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        Row(Modifier.fillMaxWidth()) {
            for (i in 0..6) {
                val currentDate = startOfWeek.plusDays(i.toLong())
                val isSelected = currentDate == innerSelectedDate

                WeeklyCalendarDayCell(
                    date = currentDate,
                    isSelected = isSelected,
                    onDateSelected = { innerSelectedDate = it }
                )
            }
        }
    }
    LaunchedEffect(weekOffset) {
        pagerState.animateScrollToPage(weekOffset + Int.MAX_VALUE / 2)
    }
    Text(
        text = "$dayOfWeek ($formattedDate)",
        modifier = Modifier.padding(8.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )

}

@Composable
private fun WeekSelector(
    weekOffset: Int,
    changeWeekOffset: (Int) -> Unit
) {
    val startOfWeek = LocalDate.now().plusWeeks(weekOffset.toLong())
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val endOfWeek = startOfWeek.plusDays(6)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { changeWeekOffset(weekOffset - 1) }, modifier = Modifier.weight(1f)) {
            Icon(Icons.Filled.KeyboardArrowLeft, null)
        }

        Text(
            text = "${startOfWeek.format(DateTimeFormatter.ofPattern("dd"))} - ${
                endOfWeek.format(
                    DateTimeFormatter.ofPattern("dd MMMM")
                )
            }",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = { changeWeekOffset(weekOffset + 1) }, modifier = Modifier.weight(1f)) {
            Icon(Icons.Filled.KeyboardArrowRight, null)
        }
    }
}

@Composable
private fun WeeklyCalendarDayCell(
    date: LocalDate,
    isSelected: Boolean,
    onDateSelected: (LocalDate) -> Unit
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val cellWidth = (screenWidthDp) / 10
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(8.dp)
            .width(cellWidth)
            .clip(RoundedCornerShape(50))
            .background(
                when {
                    isSelected -> LightColors.primary.copy(alpha = 0.17f)
                    else -> Color.Transparent
                }
            )
            .clickable { onDateSelected(date) }
            .aspectRatio(0.6f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                fontWeight = FontWeight.Bold,
                color = if (isSelected) LightColors.primary else Color.Unspecified
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = date.dayOfMonth.toString(),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) LightColors.primary else Color.Unspecified
            )
        }
    }
}