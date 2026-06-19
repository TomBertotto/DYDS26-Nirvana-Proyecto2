package edu.dyds.countries.presentation.compare

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.Currency
import edu.dyds.countries.domain.usecase.SearchCountriesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class CompareViewModelTest {

    private val searchCountriesUseCase: SearchCountriesUseCase = mockk()

    @Test
    fun `cuando cambia la primera query actualiza el estado`() = runViewModelTest {
        val viewModel = CompareViewModel(searchCountriesUseCase)

        viewModel.onFirstQueryChange("Argentina")

        assertEquals("Argentina", viewModel.uiState.value.firstQuery)
    }

    @Test
    fun `cuando cambia la segunda query actualiza el estado`() = runViewModelTest {
        val viewModel = CompareViewModel(searchCountriesUseCase)

        viewModel.onSecondQueryChange("France")

        assertEquals("France", viewModel.uiState.value.secondQuery)
    }

    @Test
    fun `cuando busca el primer pais usa el filtro all y guarda el primer resultado`() = runViewModelTest {
        val expectedCountry = country(name = "Argentina")
        coEvery { searchCountriesUseCase("Argentina", "All") } returns listOf(expectedCountry)
        val viewModel = CompareViewModel(searchCountriesUseCase)
        viewModel.onFirstQueryChange("Argentina")

        viewModel.searchFirst()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isFirstLoading)
        assertEquals(expectedCountry, viewModel.uiState.value.firstCountry)
        coVerify(exactly = 1) { searchCountriesUseCase("Argentina", "All") }
    }

    @Test
    fun `cuando busca el segundo pais usa el filtro all y guarda el primer resultado`() = runViewModelTest {
        val expectedCountry = country(name = "France")
        coEvery { searchCountriesUseCase("France", "All") } returns listOf(expectedCountry)
        val viewModel = CompareViewModel(searchCountriesUseCase)
        viewModel.onSecondQueryChange("France")

        viewModel.searchSecond()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isSecondLoading)
        assertEquals(expectedCountry, viewModel.uiState.value.secondCountry)
        coVerify(exactly = 1) { searchCountriesUseCase("France", "All") }
    }

    @Test
    fun `cuando la primera query esta vacia no busca`() = runViewModelTest {
        val viewModel = CompareViewModel(searchCountriesUseCase)

        viewModel.searchFirst()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.firstCountry)
        coVerify(exactly = 0) { searchCountriesUseCase(any(), any()) }
    }

    @Test
    fun `cuando la segunda busqueda no tiene resultados no asigna pais`() = runViewModelTest {
        coEvery { searchCountriesUseCase("Atlantis", "All") } returns emptyList()
        val viewModel = CompareViewModel(searchCountriesUseCase)
        viewModel.onSecondQueryChange("Atlantis")

        viewModel.searchSecond()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isSecondLoading)
        assertNull(viewModel.uiState.value.secondCountry)
        coVerify(exactly = 1) { searchCountriesUseCase("Atlantis", "All") }
    }

    private fun runViewModelTest(testBody: suspend kotlinx.coroutines.test.TestScope.() -> Unit) = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            testBody()
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun country(name: String = "Name"): Country = Country(
        id = name.uppercase(),
        name = name,
        officialName = "$name Official",
        capital = "Capital",
        region = "Region",
        subregion = "Subregion",
        population = 1000,
        areaKm2 = 100.0,
        currencies = listOf(Currency(code = "ARS", name = "Argentine Peso", symbol = "$")),
        languages = listOf("Spanish"),
        flagPng = "https://flagcdn.com/w320/ar.png",
        flagEmoji = "AR",
        capitalLatitude = -34.6037,
        capitalLongitude = -58.3816
    )
}
