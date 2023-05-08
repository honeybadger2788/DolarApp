package com.girlify.dolarapp.dolar.domain

import com.girlify.dolarapp.dolar.data.DollarRepository
import com.girlify.dolarapp.dolar.ui.model.DollarModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDollarsUseCase@Inject constructor(
    private val dollarRepository: DollarRepository
) {
    operator fun invoke(): Flow<List<DollarModel>> = dollarRepository.getDollars()
}