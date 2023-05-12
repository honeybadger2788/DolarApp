package com.girlify.dolarapp.dolar.domain

import com.girlify.dolarapp.dolar.data.DollarRepository
import com.girlify.dolarapp.dolar.ui.dollar.model.DollarModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class GetDollarsUseCaseTest {
    @RelaxedMockK
    private lateinit var repository: DollarRepository

    lateinit var getDollarsUseCase: GetDollarsUseCase

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        getDollarsUseCase = GetDollarsUseCase(repository)
    }

    @Test
    fun whenApiDoesNotReturnAnything_thenGetEmptyList() = runBlocking {
        coEvery { repository.getDollars() } returns flow { emit(emptyList()) }

        val response = getDollarsUseCase()

        coVerify( exactly = 1) {
            repository.getDollars()
        }

        response.collect{
            assert(emptyList<DollarModel>() == it)
        }
    }

    @Test
    fun whenApiReturnData_thenGetDollarsList() = runBlocking {
        coEvery { repository.getDollars() } returns flow{emit(getDollars())}

        val response = getDollarsUseCase()

        coVerify( exactly = 1) {
            repository.getDollars()
        }

        response.collect{
            assert(getDollars().size == it.size)
            assert(getDollars()[0].name == it[0].name)
        }
    }

    private fun getDollars(): List<DollarModel> = listOf(
        DollarModel("Bco Nacion", "450", "452", "+2.0%", ""),
        DollarModel("Ahorro", "450", "452", "+2.0%", ""),
        DollarModel("Blue", "450", "452", "7.0%", ""),
        DollarModel("MEP", "450", "452", "+2.0%", ""),
        DollarModel("CCL", "450", "452", "+2.0%", ""),
        DollarModel("Qatar", "450", "452", "+2.0%", ""),
        DollarModel("Oficial", "450", "452", "+2.0%", ""),
        DollarModel("Turista", "450", "452", "+2.0%", "")
    )
}