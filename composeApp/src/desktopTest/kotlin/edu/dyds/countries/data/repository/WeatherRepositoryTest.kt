package edu.dyds.countries.data.repository

import edu.dyds.countries.data.external.WeatherExternalSource
import edu.dyds.countries.domain.entity.Weather
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import java.io.IOException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WeatherRepositoryImplTest {

    private lateinit var weatherExternalSource: WeatherExternalSource
    private lateinit var repository: WeatherRepositoryImpl

    @BeforeTest
    fun setup() {
        weatherExternalSource = mockk()
        repository = WeatherRepositoryImpl(
            weatherExternalSource = weatherExternalSource
        )
    }

    @Test
    fun `si la fuente externa devuelve el clima lo retorna correctamente`() = runTest {
        val expectedWeather = weather(temperature = 22.5, description = "Sunny")
        coEvery { weatherExternalSource.getCurrentWeather(-38.71, -62.26) } returns expectedWeather

        val result = repository.getCurrentWeather(-38.71, -62.26)

        assertEquals(expectedWeather, result)
        coVerify(exactly = 1) { weatherExternalSource.getCurrentWeather(-38.71, -62.26) }
    }

    @Test
    fun `si la fuente externa falla por excepcion devuelve null`() = runTest {
        coEvery {
            weatherExternalSource.getCurrentWeather(0.0, 0.0)
        } throws IOException("No internet connection")

        val result = repository.getCurrentWeather(0.0, 0.0)

        assertNull(result)
        coVerify(exactly = 1) { weatherExternalSource.getCurrentWeather(0.0, 0.0) }
    }


    private fun weather(
        temperature: Double = 20.0,
        description: String = "Clear",
        humidity: Int = 50
    ): Weather = Weather(
        temperature = temperature,
        description = description,
        humidity = humidity,
        apparentTemperature = 2.6,
        windSpeed = 13.4,
        weatherCode = 1,
        isDay = false
    )
}