package com.girlify.dolarapp.dolar.ui

import com.girlify.dolarapp.dolar.ui.model.DollarModel

sealed class UiState{
    object Loading: UiState()
    object Error: UiState()
    data class Success(val dollars: List<DollarModel>): UiState()
}