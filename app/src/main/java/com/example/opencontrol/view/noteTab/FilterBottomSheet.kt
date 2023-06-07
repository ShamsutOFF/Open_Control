package com.example.opencontrol.view.noteTab

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.view.EndEditingBlock
import com.example.opencontrol.view.SelectableItemBlock
import org.koin.androidx.compose.getViewModel
import timber.log.Timber
import java.util.Calendar

class FilterBottomSheet : Screen {
    @Composable
    override fun Content() {
        FilterBottomSheetContent()
    }
}

@Composable
private fun FilterBottomSheetContent() {
    val bottomSheetNavigator = LocalBottomSheetNavigator.current
    val viewModel = getViewModel<MainViewModel>()
    val knosNames = viewModel.listOfAllKnos.map {
        it.name
    }
    var selectedKno by remember {
        mutableStateOf("нажмите для выбора")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
//        DatePickerExample()
//        SelectableItemBlock("Выберите период:", knosNames, selectedKno) {
//            Timber.d("@@@ selected = $it")
//            selectedKno = it
//        }
        SelectableItemBlock("Kонтрольно-надзорный орган", knosNames, selectedKno) {
            Timber.d("@@@ selected = $it")
            selectedKno = it
        }
        EndEditingBlock(
            textOnDismiss = "Сбросить",
            onDismiss = {
                Timber.d("@@@ Сбросить")
                bottomSheetNavigator.hide()
            },
            textOnConfirm = "Применить",
            onConfirm = {
                Timber.d("@@@ Применить")
                bottomSheetNavigator.hide()
            })
    }
}
@Composable
fun DatePickerExample() {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var selectedDateText by remember { mutableStateOf("") }

// Fetching current year, month and day
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            selectedDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
        }, year, month, dayOfMonth
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (selectedDateText.isNotEmpty()) {
                "Selected date is $selectedDateText"
            } else {
                "Please pick a date"
            }
        )

        Button(
            onClick = {
                datePicker.show()
            }
        ) {
            Text(text = "Select a date")
        }
    }
}