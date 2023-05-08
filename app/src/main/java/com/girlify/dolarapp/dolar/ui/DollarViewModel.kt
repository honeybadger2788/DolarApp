package com.girlify.dolarapp.dolar.ui

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girlify.dolarapp.dolar.data.DollarRepository
import com.girlify.dolarapp.dolar.domain.GetDollarsUseCase
import com.girlify.dolarapp.dolar.ui.UiState.Success
import com.girlify.dolarapp.dolar.ui.model.DollarModel
import com.girlify.dolarapp.dolar.ui.model.DollarOperations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DollarViewModel @Inject constructor(
    private val getDollarsUseCase: GetDollarsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _operationSelected = MutableLiveData<DollarOperations>()
    val operationSelected: LiveData<DollarOperations> = _operationSelected

    private val _dateTimeUpdated= MutableLiveData<String>()
    val dateTimeUpdated: LiveData<String> = _dateTimeUpdated

    init {
        getDollars()
    }

    private fun getDollars(){
        viewModelScope.launch {
            getDollarsUseCase().collect{
                _uiState.value = Success(it)
                _dateTimeUpdated.value = getDateTime()
            }
        }
    }

    private fun getDateTime(): String {
        val sdf =
            DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale("es", "ARG"))
        return sdf.format(Date())
    }

    fun onSelected(operation: DollarOperations) {
        _operationSelected.value = operation
    }
}