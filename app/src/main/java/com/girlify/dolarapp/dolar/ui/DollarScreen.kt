package com.girlify.dolarapp.dolar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.girlify.dolarapp.dolar.ui.model.DollarModel
import java.text.DateFormat.getDateInstance
import java.util.Date

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
    val sdf = getDateInstance()
    val currentDate = sdf.format(Date())
    TopAppBar(
        title = {
            Text(
                text = "Cotización dolar al $currentDate",
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
                .height(200.dp)
        ) {
            var amount by rememberSaveable {
                mutableStateOf(0)
            }
            TextField(
                value = amount.toString(),
                onValueChange = { amount = it.toInt() },
                label = { Text(text = "Pesos argentinos") },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            DollarList(amount)
        }
    }
}

@Composable
fun DollarList(amount: Int) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(getDollar()) {
            DollarItem(it, amount)
        }
    }
}

@Composable
fun DollarItem(dollar: DollarModel, amount: Int) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = dollar.name)
        Text(
            text = (if (amount != 0) {
                "$ ${amount / dollar.sell}"
            } else {
                "$ ${dollar.sell}"
            }).toString()
        )
    }
}

fun getDollar(): List<DollarModel> = listOf(
    DollarModel("blue", 450, 453),
    DollarModel("oficial", 450, 453),
    DollarModel("mep", 450, 453),
    DollarModel("ccl", 450, 453)
)