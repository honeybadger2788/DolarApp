package com.girlify.dolarapp.dolar.ui

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.girlify.dolarapp.dolar.ui.dollar.DOLLARS_LIST_TEST_TAG
import com.girlify.dolarapp.dolar.ui.dollar.DollarCard
import com.girlify.dolarapp.dolar.ui.dollar.OperationSelect
import com.girlify.dolarapp.dolar.ui.dollar.TEXT_FIELD_TEST_TAG
import com.girlify.dolarapp.dolar.ui.dollar.formatPesoAmount
import com.girlify.dolarapp.dolar.ui.dollar.model.DollarModel
import com.girlify.dolarapp.dolar.ui.dollar.model.DollarOperations
import com.girlify.dolarapp.dolar.ui.dollar.model.DollarOperations.Buy
import com.girlify.dolarapp.dolar.ui.dollar.model.operationToString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class DollarBodyTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenComponentStarts_thenVerifyListIsEmpty() {
        composeTestRule.setContent {
            DollarCard(emptyList(), Buy, amount)
        }

        composeTestRule.onNodeWithTag(DOLLARS_LIST_TEST_TAG)
            .onChildren()
            .assertCountEquals(0)
    }

    @Test
    fun whenComponentStarts_thenVerifyDollarsListIsDisplayed() {
        composeTestRule.setContent {
            DollarCard(getDollars(), Buy, amount)
        }

        // Comprobar que se muestra una tarjeta por cada elemento en la lista de dólares
        getDollars().forEach {
            composeTestRule.onNodeWithText(it.name).assertIsDisplayed()
        }
    }

    @Test
    fun whenSellOperationIsSelectedInDropdown_thenItIsDisplayed() {
        val operations = listOf(Buy, DollarOperations.Sell)
        var selectedOperation: DollarOperations = Buy
        var onSelectedCalled = false
        val onSelected: (DollarOperations) -> Unit = { op ->
            onSelectedCalled = true
            selectedOperation = op
        }

        composeTestRule.setContent {
            OperationSelect(selectedOperation, onSelected)
        }

        // Buscar el menú desplegable
        val menu = composeTestRule.onNodeWithTag(TEXT_FIELD_TEST_TAG)
        menu.performClick()

        // Comprobar que los elementos del menú coinciden con la lista de operaciones disponibles
        for (operation in operations) {
            composeTestRule.onNodeWithTag(operationToString(operation)).assertIsDisplayed()
        }

        // Seleccionar una opción en el menú y comprobar que el campo de texto se actualiza correctamente
        composeTestRule.onNodeWithTag(operationToString(DollarOperations.Sell)).performClick()
        composeTestRule.onNodeWithTag(TEXT_FIELD_TEST_TAG).assertIsDisplayed()
        assertTrue(onSelectedCalled)
        assertEquals(selectedOperation, DollarOperations.Sell)
    }

    @Test
    fun whenInputIsInserted_thenAmountIsUpdated() {
        var amount = ""
        val onValueChange: (String) -> Unit = {
            val itWithoutComa = it.replace(",","").trim()
            amount = itWithoutComa
        }

        composeTestRule.setContent {
            DollarCard(getDollars(), Buy, amount)
        }

        // Comprobar que se muestra una tarjeta por cada elemento en la lista de dólares
        getDollars().forEach {
            composeTestRule.onNodeWithText(it.name).assertIsDisplayed()
        }

        // Introducir una cantidad en el TextField y comprobar que se actualiza la lista de precios
        val textField = composeTestRule.onNodeWithText("Ingresa el monto a calcular")
        textField.performTextInput("1000")
        onValueChange("1000")
        assertEquals("1000", amount)
        getDollars().forEach {
            val expectedText = formatPesoAmount(1000f, it.buy)
            composeTestRule.onNodeWithTag(it.name).assertIsDisplayed().onChildAt(3)
                .assertTextEquals(expectedText)
        }
    }

    private fun getDollars(): List<DollarModel> = listOf(
        DollarModel("Blue", "450", "452", "7.0%",""),
        DollarModel("Oficial", "450", "452", "+2.0%",""),
        DollarModel("Turista", "450", "452", "+2.0%",""),
        DollarModel("CCL", "450", "452", "+2.0%",""),
        DollarModel("MEP", "450", "452", "+2.0%","")
    )
}