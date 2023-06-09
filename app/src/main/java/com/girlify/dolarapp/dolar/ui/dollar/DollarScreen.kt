package com.girlify.dolarapp.dolar.ui.dollar

import android.content.Context
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.girlify.dolarapp.core.NotificationSender
import com.girlify.dolarapp.dolar.ui.dollar.UiState.*
import com.girlify.dolarapp.dolar.ui.dollar.model.DollarModel
import com.girlify.dolarapp.dolar.ui.dollar.model.DollarOperations
import com.girlify.dolarapp.dolar.ui.dollar.model.DollarOperations.*
import com.girlify.dolarapp.dolar.ui.dollar.model.getOperations
import com.girlify.dolarapp.dolar.ui.dollar.model.operationToString
import com.girlify.dolarapp.ui.composables.ErrorComponent
import com.girlify.dolarapp.ui.composables.LoadingComponent
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

const val TEXT_FIELD_TEST_TAG = "text field test tag"
const val DOLLARS_LIST_TEST_TAG = "dollars list test tag"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DollarScreen(dollarViewModel: DollarViewModel) {
    val uiState by produceState<UiState>(initialValue = Loading) {
        dollarViewModel.uiState.collect { value = it }
    }

    LaunchedEffect(Unit){
        dollarViewModel.getDollars()
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
            .padding(8.dp),
        fontSize = 12.sp
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
        }
    )
}

@Composable
fun Body(modifier: Modifier, dollars: List<DollarModel>, dollarViewModel: DollarViewModel) {
    val operationSelected: DollarOperations by dollarViewModel.operationSelected.observeAsState(
        Sell
    )

    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary), verticalArrangement = Arrangement.Center
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
                Buy -> Text(text = "USD")
                Sell -> Text(text = "ARS")
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
                .fillMaxWidth()
                .testTag(TEXT_FIELD_TEST_TAG),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "down"
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                disabledIndicatorColor = Color.Transparent
            )
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
                },
                Modifier.testTag(operationToString(operation)))
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
            .padding(16.dp)
            .testTag(DOLLARS_LIST_TEST_TAG),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dollars) {
            DollarItem(it, amount, operationSelected)
        }
    }
}

@Composable
fun DollarItem(dollar: DollarModel, amount: Float = 0f, operationSelected: DollarOperations = Buy) {
    val amountText = when (operationSelected) {
        Buy -> formatPesoAmount(amount, dollar.buy)
        Sell -> formatDollarAmount(amount, dollar.sell)
    }

    val context = LocalContext.current
    var lastVariation by rememberSaveable { mutableStateOf(dollar.variation) }

    // La aplicación enviará una notificación cada vez que el tipo de cambio sufra una variacion
    // de +/- 5%, por única vez mientras dicho porcentaje se mantenga
    lastVariation = sendDollarNotification(context, dollar, lastVariation)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(dollar.name),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Text(
                text = dollar.name,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(4.dp))
            VariationIcon(dollar.class_variation)
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = dollar.variation,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Text(
            text = amountText,
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.Bold
        )
    }
}

fun sendDollarNotification(context: Context, dollar: DollarModel, lastVariation: String): String {
    return if (formatVariation(dollar.variation) != formatVariation(lastVariation) &&
        (formatVariation(dollar.variation) >= 5f || formatVariation(dollar.variation) <= -5f)) {
        val msg =
            if (formatVariation(dollar.variation) >= 5f) "Subió un ${dollar.variation}" else "Bajó un ${dollar.variation}"
        val createNotification = NotificationSender(context, dollar.name, msg)
        createNotification.showNotification()
        dollar.variation
    } else {
        lastVariation
    }
}

fun formatVariation(variation: String): Float {
    return variation.replace(",", ".").replace("%", "").toFloat()
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

fun formatDollarAmount(amount: Float, sellPrice: String): String {
    val exchangeRate = sellPrice.replace(",", ".").toFloat()
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    val total = if (amount != 0f) {
        format.currency = Currency.getInstance(Locale("en", "US"))
        amount / exchangeRate
    } else {
        // Para que muestre la cotización en pesos hasta que se ingrese algo en el input
        format.currency = Currency.getInstance(Locale("es", "AR"))
        exchangeRate
    }
    return format.format(total)
}

fun formatPesoAmount(amount: Float, buyPrice: String): String {
    val exchangeRate = buyPrice.replace(",", ".").toFloat()
    val total = if (amount != 0f) amount * exchangeRate else exchangeRate
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    format.currency = Currency.getInstance(Locale("es", "AR"))
    return format.format(total)
}
