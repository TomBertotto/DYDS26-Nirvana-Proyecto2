package edu.dyds.countries.data.external.openmeteo

import edu.dyds.countries.data.external.WeatherExternalSource
import edu.dyds.countries.domain.entity.Weather

class WeatherExternalSourceImpl(
    private val openMeteoWeatherExternalSource: OpenMeteoWeatherExternalSource
) : WeatherExternalSource {

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): Weather {
        return openMeteoWeatherExternalSource.getCurrentWeather(latitude, longitude).toDomain()
    }

    private fun OpenMeteoCurrent.toDomain(): Weather = Weather(
        temperature = temperature,
        apparentTemperature = apparentTemperature,
        humidity = humidity,
        windSpeed = windSpeed,
        weatherCode = weatherCode,
        description = weatherCodeDescription(weatherCode),
        isDay = isDay == 1
    )

    private fun weatherCodeDescription(code: Int): String = when (code) {
        0 -> "Clear sky"
        1 -> "Mainly clear"
        2 -> "Partly cloudy"
        3 -> "Overcast"
        45, 48 -> "Fog"
        51, 53, 55 -> "Drizzle"
        56, 57 -> "Freezing drizzle"
        61, 63, 65 -> "Rain"
        66, 67 -> "Freezing rain"
        71, 73, 75 -> "Snowfall"
        77 -> "Snow grains"
        80, 81, 82 -> "Rain showers"
        85, 86 -> "Snow showers"
        95 -> "Thunderstorm"
        96, 99 -> "Thunderstorm with hail"
        else -> "Unknown"
    }
}
