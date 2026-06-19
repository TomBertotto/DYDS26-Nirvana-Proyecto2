package edu.dyds.countries.presentation.detail

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.Currency
import edu.dyds.countries.domain.entity.Weather
import edu.dyds.countries.domain.usecase.GetCapitalWeatherUseCase
import edu.dyds.countries.domain.usecase.GetCountryDetailsUseCase
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
class DetailViewModelTest {

    private val getCountryDetailsUseCase: GetCountryDetailsUseCase = mockk()
    private val getCapitalWeatherUseCase: GetCapitalWeatherUseCase = mockk()

    @Test
    fun `cuando obtiene el detalle guarda el pais y el clima de la capital`() = runViewModelTest {
        val expectedCountry = country()
        val expectedWeather = weather()
        coEvery { getCountryDetailsUseCase("ARG") } returns expectedCountry
        coEvery { getCapitalWeatherUseCase(-34.6037, -58.3816) } returns expectedWeather
        val viewModel = DetailViewModel(getCountryDetailsUseCase, getCapitalWeatherUseCase)

        viewModel.getCountryDetail("ARG")
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.isWeatherLoading)
        assertEquals(expectedCountry, viewModel.uiState.value.country)
        assertEquals(expectedWeather, viewModel.uiState.value.weather)
        coVerify(exactly = 1) { getCountryDetailsUseCase("ARG") }
        coVerify(exactly = 1) { getCapitalWeatherUseCase(-34.6037, -58.3816) }
    }

    @Test
    fun `cuando el pais no existe no busca clima`() = runViewModelTest {
        coEvery { getCountryDetailsUseCase("XYZ") } returns null
        val viewModel = DetailViewModel(getCountryDetailsUseCase, getCapitalWeatherUseCase)

        viewModel.getCountryDetail("XYZ")
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.country)
        assertNull(viewModel.uiState.value.weather)
        coVerify(exactly = 1) { getCountryDetailsUseCase("XYZ") }
        coVerify(exactly = 0) { getCapitalWeatherUseCase(any(), any()) }
    }

    @Test
    fun `cuando el pais no tiene coordenadas no busca clima`() = runViewModelTest {
        val expectedCountry = country(latitude = null, longitude = null)
        coEvery { getCountryDetailsUseCase("ARG") } returns expectedCountry
        val viewModel = DetailViewModel(getCountryDetailsUseCase, getCapitalWeatherUseCase)

        viewModel.getCountryDetail("ARG")
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(expectedCountry, viewModel.uiState.value.country)
        assertNull(viewModel.uiState.value.weather)
        coVerify(exactly = 1) { getCountryDetailsUseCase("ARG") }
        coVerify(exactly = 0) { getCapitalWeatherUseCase(any(), any()) }
    }

    @Test
    fun `cuando carga un nuevo pais limpia el clima anterior`() = runViewModelTest {
        val argentina = country(id = "ARG", latitude = -34.6037, longitude = -58.3816)
        val france = country(id = "FRA", latitude = null, longitude = null)
        val expectedWeather = weather()
        coEvery { getCountryDetailsUseCase("ARG") } returns argentina
        coEvery { getCapitalWeatherUseCase(-34.6037, -58.3816) } returns expectedWeather
        coEvery { getCountryDetailsUseCase("FRA") } returns france
        val viewModel = DetailViewModel(getCountryDetailsUseCase, getCapitalWeatherUseCase)

        viewModel.getCountryDetail("ARG")
        advanceUntilIdle()
        viewModel.getCountryDetail("FRA")
        advanceUntilIdle()

        assertEquals(france, viewModel.uiState.value.country)
        assertNull(viewModel.uiState.value.weather)
    }

    private fun runViewModelTest(testBody: suspend kotlinx.coroutines.test.TestScope.() -> Unit) = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            testBody()
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun country(
        id: String = "ARG",
        latitude: Double? = -34.6037,
        longitude: Double? = -58.3816
    ): Country = Country(
        id = id,
        name = "Argentina",
        officialName = "Argentine Republic",
        capital = "Buenos Aires",
        region = "Americas",
        subregion = "South America",
        population = 46000000,
        areaKm2 = 2780400.0,
        currencies = listOf(Currency(code = "ARS", name = "Argentine Peso", symbol = "$")),
        languages = listOf("Spanish"),
        flagPng = "https://flagcdn.com/w320/ar.png",
        flagEmoji = "AR",
        capitalLatitude = latitude,
        capitalLongitude = longitude
    )

    private fun weather(): Weather = Weather(
        temperature = 22.5,
        apparentTemperature = 21.8,
        humidity = 60,
        windSpeed = 12.3,
        weatherCode = 1,
        description = "Mainly clear",
        isDay = true
    )
}
