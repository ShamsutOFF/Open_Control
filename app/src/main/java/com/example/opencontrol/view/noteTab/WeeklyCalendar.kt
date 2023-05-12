package com.example.opencontrol.view.noteTab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opencontrol.ui.theme.GreyBackground
import com.example.opencontrol.ui.theme.WeeklyCalendarSelectedDateBackground
import com.example.opencontrol.ui.theme.WeeklyCalendarSelectedDateText
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeeklyCalendar(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    var innerSelectedDate by remember {
        mutableStateOf(selectedDate)
    }
    var weekOffset by remember { mutableStateOf(0) }
    val startOfWeek = LocalDate.now().plusWeeks(weekOffset.toLong())
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val dayOfWeek = innerSelectedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        .capitalize(Locale.getDefault())
    val formattedDate = innerSelectedDate.format(DateTimeFormatter.ofPattern("yy.MM.dd"))

    val size by remember { mutableStateOf(Size.Zero) }
    val swipeableState = rememberSwipeableState(SwipeDirection.Initial)
    val density = LocalDensity.current
    val boxSize = 60.dp
    val width = remember(size) {
        if (size.width == 0f) {
            1f
        } else {
            size.width - with(density) { boxSize.toPx() }
        }
    }
    val scope = rememberCoroutineScope()
    if (swipeableState.isAnimationRunning) {
        DisposableEffect(Unit) {
            onDispose {
                when (swipeableState.currentValue) {
                    SwipeDirection.Right -> {
                        weekOffset--
                        println("swipe right")
                    }
                    SwipeDirection.Left -> {
                        weekOffset++
                        println("swipe left")
                    }
                    else -> {
                        return@onDispose
                    }
                }
                scope.launch {
                    // in your real app if you don't have to display offset,
                    // snap without animation
                    swipeableState.snapTo(SwipeDirection.Initial)
//                    swipeableState.animateTo(SwipeDirection.Initial)
                }
            }
        }
    }

    Column(modifier = Modifier.background(GreyBackground)) {
        WeekSelector(weekOffset) { offset ->
            weekOffset = offset
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .swipeable(
                    state = swipeableState,
                    anchors = mapOf(
                        0f to SwipeDirection.Left,
                        width / 2 to SwipeDirection.Initial,
                        width to SwipeDirection.Right,
                    ),
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                )
        ) {
            for (i in 0..6) {
                val currentDate = startOfWeek.plusDays(i.toLong())
                val isSelected = currentDate == innerSelectedDate

                WeeklyCalendarDay(
                    date = currentDate,
                    isSelected = isSelected,
                    onDateSelected = { innerSelectedDate = it }
//                    onDateSelected = onDateSelected
                )
            }
        }
        Text(
            text = "$dayOfWeek ($formattedDate)",
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun WeekSelector(
    weekOffset: Int,
    function: (Int) -> Unit
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
        IconButton(onClick = { function(weekOffset - 1) }, modifier = Modifier.weight(1f)) {
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

        IconButton(onClick = { function(weekOffset + 1) }, modifier = Modifier.weight(1f)) {
            Icon(Icons.Filled.KeyboardArrowRight, null)
        }
    }
}

@Composable
fun WeeklyCalendarDay(date: LocalDate, isSelected: Boolean, onDateSelected: (LocalDate) -> Unit) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val cellWidth = (screenWidthDp - 112.dp) / 7
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(8.dp)
            .width(cellWidth)
            .clip(RoundedCornerShape(50))
            .background(
                when {
                    isSelected -> WeeklyCalendarSelectedDateBackground
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
                color = if (isSelected) WeeklyCalendarSelectedDateText else Color.Unspecified
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = date.dayOfMonth.toString(),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) WeeklyCalendarSelectedDateText else Color.Unspecified
            )
        }
    }
}