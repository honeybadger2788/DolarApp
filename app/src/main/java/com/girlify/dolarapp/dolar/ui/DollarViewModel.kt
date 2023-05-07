package com.girlify.dolarapp.dolar.ui

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girlify.dolarapp.dolar.data.DollarRepository
import com.girlify.dolarapp.dolar.ui.model.DollarModel
import com.girlify.dolarapp.dolar.ui.model.DollarOperations
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

    private val _operationSelected = MutableLiveData<DollarOperations>()
    val operationSelected: LiveData<DollarOperations> = _operationSelected

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

    fun onSelected(operation: DollarOperations) {
        _operationSelected.value = operation
    }
}