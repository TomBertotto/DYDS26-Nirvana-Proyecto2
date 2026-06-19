package edu.dyds.countries.domain.usecase

import edu.dyds.countries.domain.entity.Weather
import edu.dyds.countries.domain.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetCapitalWeatherUseCaseImplTest {

    private lateinit var repository: WeatherRepository
    private lateinit var useCase: GetCapitalWeatherUseCaseImpl

    @BeforeTest
    fun setup() {
        repository = mockk()
        useCase = GetCapitalWeatherUseCaseImpl(repository)
    }

    @Test
    fun `si existe clima delega por coordenadas y devuelve el clima actual`() = runTest {
        val expectedWeather = weather()
        coEvery { repository.getCurrentWeather(latitude = -34.6037, longitude = -58.3816) } returns expectedWeather

        val result = useCase(latitude = -34.6037, longitude = -58.3816)

        assertEquals(expectedWeather, result)
        coVerify(exactly = 1) { repository.getCurrentWeather(latitude = -34.6037, longitude = -58.3816) }
    }

    @Test
    fun `si no existe clima devuelve null`() = runTest {
        coEvery { repository.getCurrentWeather(latitude = 0.0, longitude = 0.0) } returns null

        val result = useCase(latitude = 0.0, longitude = 0.0)

        assertNull(result)
        coVerify(exactly = 1) { repository.getCurrentWeather(latitude = 0.0, longitude = 0.0) }
    }

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
