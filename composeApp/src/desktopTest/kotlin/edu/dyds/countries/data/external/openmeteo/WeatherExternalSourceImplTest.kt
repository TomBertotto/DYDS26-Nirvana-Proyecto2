package edu.dyds.countries.data.external.openmeteo

import edu.dyds.countries.domain.entity.Weather
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WeatherExternalSourceImplTest {

    private lateinit var openMeteoWeatherExternalSource: OpenMeteoWeatherExternalSource
    private lateinit var externalSource: WeatherExternalSourceImpl

    @BeforeTest
    fun setup() {
        openMeteoWeatherExternalSource = mockk()
        externalSource = WeatherExternalSourceImpl(openMeteoWeatherExternalSource)
    }

    @Test
    fun `getCurrentWeather convierte la respuesta de OpenMeteoCurrent a Weather correctamente`() = runTest {
        val remoteWeather = mockk<OpenMeteoCurrent>().apply {
            coEvery { temperature } returns 24.5
            coEvery { apparentTemperature } returns 26.0
            coEvery { humidity } returns 60
            coEvery { windSpeed } returns 12.5
            coEvery { weatherCode } returns 0
            coEvery { isDay } returns 1
        }
        coEvery { openMeteoWeatherExternalSource.getCurrentWeather(-34.60, -58.38) } returns remoteWeather

        val result = externalSource.getCurrentWeather(-34.60, -58.38)

        val expected = Weather(
            temperature = 24.5,
            apparentTemperature = 26.0,
            humidity = 60,
            windSpeed = 12.5,
            weatherCode = 0,
            description = "Clear sky",
            isDay = true
        )

        assertEquals(expected, result)
    }

    @Test
    fun `getCurrentWeather mapea correctamente codigos de clima complejos y noche`() = runTest {
        val remoteWeather = mockk<OpenMeteoCurrent>().apply {
            coEvery { temperature } returns 5.0
            coEvery { apparentTemperature } returns 2.0
            coEvery { humidity } returns 95
            coEvery { windSpeed } returns 25.0
            coEvery { weatherCode } returns 3
            coEvery { isDay } returns 0
        }
        coEvery { openMeteoWeatherExternalSource.getCurrentWeather(51.50, -0.12) } returns remoteWeather

        val result = externalSource.getCurrentWeather(51.50, -0.12)

        val expected = Weather(
            temperature = 5.0,
            apparentTemperature = 2.0,
            humidity = 95,
            windSpeed = 25.0,
            weatherCode = 3,
            description = "Overcast",
            isDay = false
        )

        assertEquals(expected, result)
    }

    @Test
    fun `getCurrentWeather asigna Unknown si el codigo de clima no esta registrado`() = runTest {
        val remoteWeather = mockk<OpenMeteoCurrent>().apply {
            coEvery { temperature } returns 15.0
            coEvery { apparentTemperature } returns 15.0
            coEvery { humidity } returns 50
            coEvery { windSpeed } returns 5.0
            coEvery { weatherCode } returns 999
            coEvery { isDay } returns 1
        }
        coEvery { openMeteoWeatherExternalSource.getCurrentWeather(0.0, 0.0) } returns remoteWeather

        val result = externalSource.getCurrentWeather(0.0, 0.0)

        assertEquals("Unknown", result.description)
    }
}
