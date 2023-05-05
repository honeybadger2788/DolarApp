package com.girlify.dolarapp.dolar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girlify.dolarapp.dolar.data.DollarRepository
import com.girlify.dolarapp.dolar.ui.model.DollarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DollarViewModel: ViewModel() {
    private val repository = DollarRepository()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        getDollars()
    }

    private fun getDollars(){
        viewModelScope.launch(Dispatchers.IO) {
            val deferreds = listOf(
                async { repository.getDollarAhorro() },
                async { repository.getDollarBlue() },
                async { repository.getDollarOficial() },
                async { repository.getDollarBcoNacion() },
                async { repository.getDollarQatar() },
                async { repository.getDollarMEP() },
                async { repository.getDollarCCL() },
                async { repository.getDollarTurista() }
            )
            val responses = deferreds.awaitAll()
            responses.let {
                _uiState.value = UiState.Success(it as List<DollarModel>)
            }
        }
    }
}