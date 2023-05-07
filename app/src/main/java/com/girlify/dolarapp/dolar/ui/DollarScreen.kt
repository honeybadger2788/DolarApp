package com.girlify.dolarapp.dolar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.girlify.dolarapp.dolar.ui.model.DollarModel
import com.girlify.dolarapp.dolar.ui.model.DollarOperations
import com.girlify.dolarapp.dolar.ui.model.DollarOperations.*
import com.girlify.dolarapp.dolar.ui.model.getOperations
import com.girlify.dolarapp.dolar.ui.model.operationToString
import com.girlify.dolarapp.ui.composables.ErrorComponent
import com.girlify.dolarapp.ui.composables.LoadingComponent
import java.text.DateFormat.SHORT
import java.text.DateFormat.getDateTimeInstance
import java.text.NumberFormat
import java.util.Currency
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DollarScreen(dollarViewModel: DollarViewModel) {
    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            Footer()
        }
    ) {padding ->
        val uiState by produceState<UiState>(initialValue = UiState.Loading) {
            dollarViewModel.uiState.collect { value = it }
        }

        when(uiState){
            UiState.Error -> ErrorComponent(msg = "No se pudieron actualizar los valores")
            UiState.Loading -> LoadingComponent()
            is UiState.Success -> Body(
                Modifier.padding(padding),
                (uiState as UiState.Success).dollars,
                dollarViewModel
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Footer() {
    Text(
        text = "Fuente: Ámbito Financiero",
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    val sdf = getDateTimeInstance(SHORT,SHORT, Locale("es","ARG"))
    val currentDate = sdf.format(Date())
    TopAppBar(
        title = {
            Text(
                text = "Última actualización: $currentDate",
                modifier = Modifier.fillMaxWidth()
            )
        },
    )
}

@Composable
fun Body(modifier: Modifier, dollars: List<DollarModel>, dollarViewModel: DollarViewModel) {
    val operationSelected: DollarOperations by dollarViewModel.operationSelected.observeAsState(
        Compra
    )

    Column(
        modifier
            .fillMaxSize()
            .background(Color(0xFF009688)), verticalArrangement = Arrangement.Center
    ) {

        OperationSelect(operationSelected) { dollarViewModel.onSelected(it) }
        DollarCard(dollars, operationSelected)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DollarCard(dollars: List<DollarModel>, operationSelected: DollarOperations) {
    Card(
        Modifier
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        var amount by rememberSaveable {
            mutableStateOf("")
        }
        TextField(
            value = pesosFormatter(amount),
            onValueChange = {
                val itWithoutComa = it.replace(",","").trim()
                amount = itWithoutComa
            },
            singleLine = true,
            label = { Text(text = "Ingresa el monto a calcular")},
            trailingIcon = { when(operationSelected){
                Compra -> Text(text = "ARG")
                Venta -> Text(text = "USD")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        DollarList(
            (if (amount.isEmpty()){
                0f
            } else {
                amount.toFloat()
            }), dollars, operationSelected
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationSelect(operationSelected: DollarOperations, onSelected: (DollarOperations) -> Unit) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    Column(Modifier.padding(16.dp)) {
        TextField(
            value = operationToString(operationSelected),
            onValueChange = { onSelected(operationSelected) },
            enabled = false,
            readOnly = true,
            modifier = Modifier
                .clickable { expanded = true }
                .fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            getOperations().forEach { operation ->
                DropdownMenuItem(text = {
                    Text(
                        text =
                        operationToString(operation)
                    )
                }, onClick = {
                    expanded = false
                    onSelected(operation)
                })
            }
        }
    }
}

fun pesosFormatter(total: String): String {
    val format: NumberFormat = NumberFormat.getInstance()
    format.maximumFractionDigits = 2
    format.maximumIntegerDigits = 12
    total.trim()
    return if (total == ""){
        ""
    } else {
        val totalSinComa = total.replace(",","")
        format.format(totalSinComa.toFloat())
    }
}

@Composable
fun DollarList(amount: Float, dollars: List<DollarModel>, operationSelected: DollarOperations) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dollars) {
            DollarItem(it, amount)
        }
    }
}

@Composable
fun DollarItem(dollar: DollarModel, amount: Float) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = dollar.name, fontSize = 20.sp, color = Color(0xFF004D40))
        Text(
            text = (if (amount != 0f) {
                dollarFormatter(amount / dollar.sell.replace(",", ".").toFloat())
            } else {
                dollarFormatter(dollar.sell.replace(",", ".").toFloat())
            }),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF004D40)
        )
    }
}

fun dollarFormatter(total: Float): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    format.currency = Currency.getInstance(Locale("en", "US"))
    return format.format(total)
}