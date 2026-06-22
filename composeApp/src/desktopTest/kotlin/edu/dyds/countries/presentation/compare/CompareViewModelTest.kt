package edu.dyds.countries.presentation.compare

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.Currency
import edu.dyds.countries.domain.entity.SearchCriteria
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
    fun `cuando busca el primer pais usa el filtro name y guarda el primer resultado`() = runViewModelTest {
        val expectedCountry = country(name = "Argentina")
        coEvery { searchCountriesUseCase("Argentina", SearchCriteria.NAME) } returns listOf(expectedCountry)
        val viewModel = CompareViewModel(searchCountriesUseCase)
        viewModel.onFirstQueryChange("Argentina")

        viewModel.searchFirst()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isFirstLoading)
        assertEquals(expectedCountry, viewModel.uiState.value.firstCountry)
        coVerify(exactly = 1) { searchCountriesUseCase("Argentina", SearchCriteria.NAME) }
    }

    @Test
    fun `cuando busca el segundo pais usa el filtro name y guarda el primer resultado`() = runViewModelTest {
        val expectedCountry = country(name = "France")
        coEvery { searchCountriesUseCase("France", SearchCriteria.NAME) } returns listOf(expectedCountry)
        val viewModel = CompareViewModel(searchCountriesUseCase)
        viewModel.onSecondQueryChange("France")

        viewModel.searchSecond()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isSecondLoading)
        assertEquals(expectedCountry, viewModel.uiState.value.secondCountry)
        coVerify(exactly = 1) { searchCountriesUseCase("France", SearchCriteria.NAME) }
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
        coEvery { searchCountriesUseCase("Atlantis", SearchCriteria.NAME) } returns emptyList()
        val viewModel = CompareViewModel(searchCountriesUseCase)
        viewModel.onSecondQueryChange("Atlantis")

        viewModel.searchSecond()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isSecondLoading)
        assertNull(viewModel.uiState.value.secondCountry)
        coVerify(exactly = 1) { searchCountriesUseCase("Atlantis", SearchCriteria.NAME) }
    }

    @Test
    fun `cuando la primera busqueda es exitosa limpia la query`() = runViewModelTest {
        val expectedCountry = country(name = "Argentina")
        coEvery { searchCountriesUseCase("Argentina", SearchCriteria.NAME) } returns listOf(expectedCountry)
        val viewModel = CompareViewModel(searchCountriesUseCase)
        viewModel.onFirstQueryChange("Argentina")

        viewModel.searchFirst()
        advanceUntilIdle()

        assertEquals("", viewModel.uiState.value.firstQuery)
        assertNull(viewModel.uiState.value.firstError)
        assertEquals(expectedCountry, viewModel.uiState.value.firstCountry)
    }

    @Test
    fun `cuando la primera busqueda no tiene resultados asigna el error y conserva la query`() = runViewModelTest {
        coEvery { searchCountriesUseCase("Atlantis", SearchCriteria.NAME) } returns emptyList()
        val viewModel = CompareViewModel(searchCountriesUseCase)
        viewModel.onFirstQueryChange("Atlantis")

        viewModel.searchFirst()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isFirstLoading)
        assertNull(viewModel.uiState.value.firstCountry)
        assertEquals("Country not found", viewModel.uiState.value.firstError)
        assertEquals("Atlantis", viewModel.uiState.value.firstQuery)
    }

    @Test
    fun `cuando la segunda busqueda no tiene resultados asigna el error y conserva la query`() = runViewModelTest {
        coEvery { searchCountriesUseCase("Atlantis", SearchCriteria.NAME) } returns emptyList()
        val viewModel = CompareViewModel(searchCountriesUseCase)
        viewModel.onSecondQueryChange("Atlantis")

        viewModel.searchSecond()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isSecondLoading)
        assertNull(viewModel.uiState.value.secondCountry)
        assertEquals("Country not found", viewModel.uiState.value.secondError)
        assertEquals("Atlantis", viewModel.uiState.value.secondQuery)
    }

    @Test
    fun `cuando la primera busqueda lanza una excepcion asigna el error y desactiva la carga`() = runViewModelTest {
        coEvery { searchCountriesUseCase("Argentina", SearchCriteria.NAME) } throws RuntimeException("Network error")
        val viewModel = CompareViewModel(searchCountriesUseCase)
        viewModel.onFirstQueryChange("Argentina")

        viewModel.searchFirst()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isFirstLoading)
        assertNull(viewModel.uiState.value.firstCountry)
        assertEquals("Network error", viewModel.uiState.value.firstError)
    }

    @Test
    fun `cuando la busqueda lanza una excepcion sin mensaje asigna un error generico`() = runViewModelTest {
        coEvery { searchCountriesUseCase("Argentina", SearchCriteria.NAME) } throws RuntimeException()
        val viewModel = CompareViewModel(searchCountriesUseCase)
        viewModel.onFirstQueryChange("Argentina")

        viewModel.searchFirst()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isFirstLoading)
        assertEquals("An error occurred", viewModel.uiState.value.firstError)
    }

    @Test
    fun `cuando una busqueda exitosa sucede tras un error limpia el error previo`() = runViewModelTest {
        val expectedCountry = country(name = "Argentina")
        coEvery { searchCountriesUseCase("Atlantis", SearchCriteria.NAME) } returns emptyList()
        coEvery { searchCountriesUseCase("Argentina", SearchCriteria.NAME) } returns listOf(expectedCountry)
        val viewModel = CompareViewModel(searchCountriesUseCase)

        viewModel.onFirstQueryChange("Atlantis")
        viewModel.searchFirst()
        advanceUntilIdle()

        viewModel.onFirstQueryChange("Argentina")
        viewModel.searchFirst()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.firstError)
        assertEquals(expectedCountry, viewModel.uiState.value.firstCountry)
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
