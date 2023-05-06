package com.girlify.dolarapp.dolar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girlify.dolarapp.dolar.data.DollarRepository
import com.girlify.dolarapp.dolar.ui.model.DollarModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DollarViewModel @Inject constructor(
    private val repository: DollarRepository
) : ViewModel() {

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