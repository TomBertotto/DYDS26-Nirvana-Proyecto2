package edu.dyds.countries.presentation.home

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
class HomeViewModelTest {

    private val searchCountriesUseCase: SearchCountriesUseCase = mockk()

    @Test
    fun `cuando cambia la query actualiza el estado`() = runViewModelTest {
        val viewModel = HomeViewModel(searchCountriesUseCase)

        viewModel.onQueryChange("Argentina")

        assertEquals("Argentina", viewModel.uiState.value.query)
    }

    @Test
    fun `cuando cambia el criterio actualiza el estado`() = runViewModelTest {
        val viewModel = HomeViewModel(searchCountriesUseCase)

        viewModel.onCriteriaChange(SearchCriteria.REGION)

        assertEquals(SearchCriteria.REGION, viewModel.uiState.value.selectedCriteria)
    }

    @Test
    fun `cuando cambia el criterio a all limpia la query`() = runViewModelTest {
        val viewModel = HomeViewModel(searchCountriesUseCase)
        viewModel.onQueryChange("Europe")

        viewModel.onCriteriaChange(SearchCriteria.ALL)

        assertEquals("", viewModel.uiState.value.query)
        assertEquals(SearchCriteria.ALL, viewModel.uiState.value.selectedCriteria)
    }

    @Test
    fun `cuando carga paises iniciales busca con query vacia y criterio all`() = runViewModelTest {
        val expectedCountries = listOf(country(name = "Argentina"), country(name = "France"))
        coEvery { searchCountriesUseCase("", SearchCriteria.ALL) } returns expectedCountries
        val viewModel = HomeViewModel(searchCountriesUseCase)

        viewModel.loadInitialCountries()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(expectedCountries, viewModel.uiState.value.countries)
        coVerify(exactly = 1) { searchCountriesUseCase("", SearchCriteria.ALL) }
    }

    @Test
    fun `cuando busca usa la query y el criterio seleccionado`() = runViewModelTest {
        val expectedCountries = listOf(country(name = "Canada"))
        coEvery { searchCountriesUseCase("English", SearchCriteria.LANGUAGE) } returns expectedCountries
        val viewModel = HomeViewModel(searchCountriesUseCase)
        viewModel.onCriteriaChange(SearchCriteria.LANGUAGE)
        viewModel.onQueryChange("English")

        viewModel.search()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(expectedCountries, viewModel.uiState.value.countries)
        coVerify(exactly = 1) { searchCountriesUseCase("English", SearchCriteria.LANGUAGE) }
    }

    @Test
    fun `cuando la busqueda no tiene resultados conserva la lista anterior`() = runViewModelTest {
        val initialCountries = listOf(country(name = "Argentina"))
        coEvery { searchCountriesUseCase("", SearchCriteria.ALL) } returns initialCountries
        coEvery { searchCountriesUseCase("Atlantis", SearchCriteria.NAME) } returns emptyList()
        val viewModel = HomeViewModel(searchCountriesUseCase)
        viewModel.loadInitialCountries()
        advanceUntilIdle()
        viewModel.onCriteriaChange(SearchCriteria.NAME)
        viewModel.onQueryChange("Atlantis")

        viewModel.search()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(initialCountries, viewModel.uiState.value.countries)
        coVerify(exactly = 1) { searchCountriesUseCase("Atlantis", SearchCriteria.NAME) }
    }

    @Test
    fun `cuando la carga inicial lanza una excepcion asigna el error y desactiva la carga`() = runViewModelTest {
        coEvery { searchCountriesUseCase("", SearchCriteria.ALL) } throws RuntimeException("Network error")
        val viewModel = HomeViewModel(searchCountriesUseCase)

        viewModel.loadInitialCountries()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("Network error", viewModel.uiState.value.error)
    }

    @Test
    fun `cuando la busqueda exitosa sucede tras un error limpia el error previo`() = runViewModelTest {
        val expectedCountries = listOf(country(name = "Argentina"))
        coEvery { searchCountriesUseCase("", SearchCriteria.ALL) } throws RuntimeException("Network error")
        coEvery { searchCountriesUseCase("Argentina", SearchCriteria.NAME) } returns expectedCountries
        val viewModel = HomeViewModel(searchCountriesUseCase)

        viewModel.loadInitialCountries()
        advanceUntilIdle()
        viewModel.onCriteriaChange(SearchCriteria.NAME)
        viewModel.onQueryChange("Argentina")
        viewModel.search()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.error)
        assertEquals(expectedCountries, viewModel.uiState.value.countries)
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
