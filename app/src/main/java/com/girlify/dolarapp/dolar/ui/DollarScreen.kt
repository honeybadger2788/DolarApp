package com.girlify.dolarapp.dolar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.girlify.dolarapp.dolar.ui.UiState.*
import com.girlify.dolarapp.dolar.ui.model.DollarModel
import com.girlify.dolarapp.dolar.ui.model.DollarOperations
import com.girlify.dolarapp.dolar.ui.model.DollarOperations.*
import com.girlify.dolarapp.dolar.ui.model.getOperations
import com.girlify.dolarapp.dolar.ui.model.operationToString
import com.girlify.dolarapp.ui.composables.ErrorComponent
import com.girlify.dolarapp.ui.composables.LoadingComponent
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DollarScreen(dollarViewModel: DollarViewModel) {
    val uiState by produceState<UiState>(initialValue = Loading) {
        dollarViewModel.uiState.collect { value = it }
    }

    val dateTimeUpdated: String by dollarViewModel.dateTimeUpdated.observeAsState("")
    Scaffold(
        topBar = {
            TopBar(dateTimeUpdated)
        },
        bottomBar = {
            Footer()
        }
    ) {padding ->
        when(uiState){
            Error -> ErrorComponent(msg = "No se pudieron actualizar los valores")
            Loading -> LoadingComponent()
            is Success -> Body(
                Modifier.padding(padding),
                (uiState as Success).dollars,
                dollarViewModel
            )
        }
    }
}

@Composable
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
fun TopBar(dateTimeUpdated: String) {
    TopAppBar(
        title = {
            Text(
                text = "Última actualización: $dateTimeUpdated",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp
            )
        },
    )
}

@Composable
fun Body(modifier: Modifier, dollars: List<DollarModel>, dollarViewModel: DollarViewModel) {
    val operationSelected: DollarOperations by dollarViewModel.operationSelected.observeAsState(
        Venta
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
            value = inputFormatter(amount),
            onValueChange = {
                val itWithoutComa = it.replace(",","").trim()
                amount = itWithoutComa
            },
            singleLine = true,
            label = { Text(text = "Ingresa el monto a calcular")},
            trailingIcon = { when(operationSelected){
                Compra -> Text(text = "USD")
                Venta -> Text(text = "ARS")
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
            label = { Text(text = "Tipo de cambio") },
            enabled = false,
            readOnly = true,
            shape = ShapeDefaults.Medium,
            textStyle = TextStyle(Color.Black),
            modifier = Modifier
                .clickable { expanded = true }
                .fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "down"
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier.fillMaxWidth()
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

fun inputFormatter(total: String): String {
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
            DollarItem(it, amount, operationSelected)
        }
    }
}

@Composable
fun DollarItem(dollar: DollarModel, amount: Float = 0f, operationSelected: DollarOperations = Compra) {
    val amountText = when (operationSelected) {
        Compra -> formatPesoAmount(amount, dollar.buy)
        Venta -> formatDollarAmount(amount, dollar.sell)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Text(
                text = dollar.name,
                color = Color(0xFF004D40),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(4.dp))
            VariationIcon(dollar.class_variation)
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = dollar.variation,
                color = Color(0xFF004D40)
            )
        }
        Text(
            text = amountText,
            color = Color(0xFF004D40),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun VariationIcon(classVariation: String) {
    Icon(imageVector =
        when(classVariation){
            "up" -> Icons.Default.TrendingUp
            "down" -> Icons.Default.TrendingDown
            else -> Icons.Default.TrendingFlat
    }, contentDescription = "variation")
}

private fun formatDollarAmount(amount: Float, sellPrice: String): String {
    val exchangeRate = sellPrice.replace(",", ".").toFloat()
    val total = if (amount != 0f) amount / exchangeRate else exchangeRate
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    format.currency = Currency.getInstance(Locale("en", "US"))
    return format.format(total)
}

private fun formatPesoAmount(amount: Float, buyPrice: String): String {
    val exchangeRate = buyPrice.replace(",", ".").toFloat()
    val total = if (amount != 0f) amount * exchangeRate else exchangeRate
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    format.currency = Currency.getInstance(Locale("es", "AR"))
    return format.format(total)
}
