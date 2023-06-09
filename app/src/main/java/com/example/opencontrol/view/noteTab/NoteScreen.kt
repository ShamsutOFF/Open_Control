package com.example.opencontrol.view.noteTab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.model.UserRole
import com.example.opencontrol.model.networkDTOs.AppointmentsInLocalDateTime
import com.example.opencontrol.ui.theme.GreenStatus
import com.example.opencontrol.ui.theme.GreyBackground
import com.example.opencontrol.ui.theme.LightColors
import com.example.opencontrol.ui.theme.YellowStatus
import org.koin.androidx.compose.getViewModel
import timber.log.Timber
import java.time.format.DateTimeFormatter
import java.util.Locale


class NoteScreen : Screen {
    @Composable
    override fun Content() {
        NoteScreenContent()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun NoteScreenContent() {
    Timber.d("@@@ NoteScreenContent()")
    val viewModel = getViewModel<MainViewModel>()

    val refreshing by remember { mutableStateOf(false) }
    val state = rememberPullRefreshState(refreshing, {
        if (viewModel.userRole == UserRole.BUSINESS) {
            viewModel.getAllBusinessAppointments()
            viewModel.getBusinessUserInfo()
        } else {
            viewModel.getInspectorUserInfo()
            viewModel.getAllInspectorAppointments()
        }
    })
    val appointments =
        if (viewModel.userRole == UserRole.BUSINESS) {
            Timber.d("@@@ NoteScreenContent() viewModel.getAllBusinessAppointmentsInLDT()")
            viewModel.getAllBusinessAppointmentsInLDT()
        } else {
            Timber.d("@@@ NoteScreenContent() viewModel.getAllInspectorAppointmentsInLDT()")
            viewModel.getAllInspectorAppointmentsInLDT()
        }
    val listState = rememberLazyListState()
    val selectedIndex =
        appointments.indexOfFirst { it.time.toLocalDate() >= viewModel.selectedDate }
    val scrollPosition =
        selectedIndex.takeIf { it != -1 } ?: appointments.lastIndex.takeIf { it != -1 } ?: 0

    LaunchedEffect(key1 = true) {
        if (viewModel.userRole == UserRole.BUSINESS) {
            viewModel.getAllBusinessAppointments()
            viewModel.getBusinessUserInfo()
        } else {
            viewModel.getInspectorUserInfo()
            viewModel.getAllInspectorAppointments()
        }
    }
    LaunchedEffect(key1 = viewModel.selectedDate) {
        Timber.d("@@@ scrollPosition = $scrollPosition")
        listState.animateScrollToItem(index = scrollPosition)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state)
    ) {
        Column(
            modifier = Modifier
                .background(GreyBackground)
        ) {
            MonthlyCalendar(
                selectedDate = viewModel.selectedDate,
                markedDateList = appointments.map { it.time.toLocalDate() },
                onDateSelected = viewModel::changeSelectedDate
            )
            MyNotesAndButtonsRow()
            LazyColumn(state = listState) {
                items(appointments) { note ->
                    NoteCard(note = note)
                }
            }
        }
        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
private fun MyNotesAndButtonsRow() {
    val navigator = LocalNavigator.currentOrThrow
    val bottomSheetNavigator = LocalBottomSheetNavigator.current
    val viewModel = getViewModel<MainViewModel>()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Мои записи",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
        if (viewModel.userRole == UserRole.BUSINESS) {
            Button(onClick = { navigator.push(NewNoteScreen()) }) {
                Text(
                    text = "новая запись",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        Button(onClick = { bottomSheetNavigator.show(FilterBottomSheet()) }) {
            Text(
                text = "фильтр",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun NoteCard(note: AppointmentsInLocalDateTime) {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = getViewModel<MainViewModel>()
    val businessUserInfoFlow = viewModel.getBusinessUserInfoFromInspector(note.businessUserId)
    val businessUserInfo by businessUserInfoFlow.collectAsState(null)
    LaunchedEffect(businessUserInfoFlow) {
        if (viewModel.userRole == UserRole.INSPECTION) {
            businessUserInfoFlow.collect { userInfo ->
                Timber.d("@@@ LaunchedEffect businessUserInfo = $userInfo")
            }
        }
    }
    Card(
        onClick = { navigator.push(NoteInfoScreen(note.id)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightColors.onPrimary,
        )
    ) {
        if (viewModel.userRole == UserRole.BUSINESS) {
            Text(
                text = note.knoName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = note.measureName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraLight
            )
        } else {
            Text(
                text = "${businessUserInfo?.lastName} ${businessUserInfo?.firstName} ${businessUserInfo?.surName}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "email: ${businessUserInfo?.email}  ИНН: ${businessUserInfo?.inn}  СНИЛС: ${businessUserInfo?.snils}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraLight
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val formattedTime = note.time.format(formatter)
            val formattedPlusHour = note.time.plusHours(1).format(formatter)

            val formatter1 = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru"))
            Text(
                text = note.time.format(formatter1),
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraLight
            )
            Text(
                text = "   $formattedTime - $formattedPlusHour",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraLight
            )
        }
        if (note.status == "SELECTED") {
            Text(
                text = "Ожидает подтверждения",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = LightColors.onPrimary,
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = YellowStatus,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(4.dp)

            )
        } else {
            Text(
                text = "Запись подтверждена",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = LightColors.onPrimary,
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = GreenStatus,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(4.dp)
            )
        }

    }
}