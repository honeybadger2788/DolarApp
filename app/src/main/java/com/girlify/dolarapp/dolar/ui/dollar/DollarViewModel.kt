package com.girlify.dolarapp.dolar.ui.dollar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girlify.dolarapp.dolar.domain.GetDollarsUseCase
import com.girlify.dolarapp.dolar.ui.dollar.UiState.Success
import com.girlify.dolarapp.dolar.ui.dollar.model.DollarOperations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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


    fun getDollars(){
        viewModelScope.launch {
            getDollarsUseCase().collect{
                _uiState.value = Success(it)
                // Hará que el reloj esté actualizado con la última consulta
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