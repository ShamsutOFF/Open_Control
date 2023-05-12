package com.example.opencontrol.view.noteTab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opencontrol.ui.theme.Invisible
import com.example.opencontrol.ui.theme.LightColors
import com.example.opencontrol.ui.theme.WeeklyCalendarSelectedDateBackground
import com.example.opencontrol.ui.theme.WeeklyCalendarSelectedDateText
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun MyCalendarView(
    selectedDate: LocalDate,
    markedDateList: List<LocalDate>,
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
        DaysGrid(currentMonth, markedDateList, selectedDate, onDateSelected)
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

@Composable
private fun DaysGrid(
    currentMonth: YearMonth,
    markedDateList: List<LocalDate>,
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
                val isSelected = date == selectedDate
                val isMarked = markedDateList.contains(date)
                DayView(
                    date = date,
                    isMarked = isMarked,
                    isSelected = isSelected,
                    onDateSelected = onDateSelected
                )
            }
        }
    }
}

@Composable
fun DayView(
    date: LocalDate,
    isMarked: Boolean,
    isSelected: Boolean,
    onDateSelected: (LocalDate) -> Unit
) {
    val isToday = date == LocalDate.now()

    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val cellWidth = screenWidthDp / 7
    Timber.d("@@@ 1 cellWidth= $cellWidth")

    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(cellWidth)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> LightColors.primary
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
        if (isMarked) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(color = if (!isSelected) LightColors.primary else LightColors.onPrimary)
                        .align(Alignment.TopCenter)
                )
            }
        }
    }
}

enum class SwipeDirection(val raw: Int) {
    Left(0),
    Initial(1),
    Right(2),
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TestScreen() {
    var size by remember { mutableStateOf(Size.Zero) }
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
                        println("swipe right")
                    }
                    SwipeDirection.Left -> {
                        println("swipe left")
                    }
                    else -> {
                        return@onDispose
                    }
                }
                scope.launch {
                    // in your real app if you don't have to display offset,
                    // snap without animation
                    // swipeableState.snapTo(SwipeDirection.Initial)
                    swipeableState.animateTo(SwipeDirection.Initial)
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged { size = Size(it.width.toFloat(), it.height.toFloat()) }
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
            .background(Color.LightGray)
    ) {
        Box(
            Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .size(boxSize)
                .background(Color.DarkGray)
        )
    }
}

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
    Column {
        WeekSelector(weekOffset) { offset ->
            weekOffset = offset
        }
        Row(modifier = Modifier.fillMaxWidth()) {
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

enum class WeekDays(val shortName: String) {
    MONDAY("ПН"),
    TUESDAY("ВТ"),
    WEDNESDAY("СР"),
    THURSDAY("ЧТ"),
    FRIDAY("ПТ"),
    SATURDAY("СБ"),
    SUNDAY("ВС")
}