package com.girlify.dolarapp.dolar.ui

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.girlify.dolarapp.dolar.ui.model.DollarModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Formatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun DollarScreen() {
    Scaffold(
        topBar = {
            TopBar()
        }
    ) {
        Body(Modifier.padding(it))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    val sdf = SimpleDateFormat("dd/MM/yy HH:mm")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Body(modifier: Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .background(Color(0xFF009688)), verticalArrangement = Arrangement.Center
    ) {
        Card(
            Modifier
                .padding(16.dp)
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
                leadingIcon = { Text(text = "ARG") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            DollarList(
                (if (amount.isEmpty()){
                    0f
                } else {
                    amount.toFloat()
                })
            )
        }
    }
}

fun pesosFormatter(total: String): String {
    val format: NumberFormat = NumberFormat.getInstance()
    format.minimumFractionDigits = 2
    format.maximumFractionDigits = 2
    format.maximumIntegerDigits = 8
    total.trim()
    return if (total == ""){
        ""
    } else {
        val totalSinComa = total.replace(",","")
        format.format(totalSinComa.toFloat())
    }
}

@Composable
fun DollarList(amount: Float) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(getDollar()) {
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
                dollarFormatter(amount/dollar.sell)
            } else {
                dollarFormatter(dollar.sell.toFloat())
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

fun getDollar(): List<DollarModel> = listOf(
    DollarModel("Blue", 450, 453),
    DollarModel("Oficial", 450, 453),
    DollarModel("Mep", 450, 453),
    DollarModel("CCL", 450, 453)
)