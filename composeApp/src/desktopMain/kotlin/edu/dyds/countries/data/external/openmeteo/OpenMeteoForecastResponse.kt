package edu.dyds.countries.data.external.openmeteo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoForecastResponse(
    val current: OpenMeteoCurrent = OpenMeteoCurrent()
)

@Serializable
data class OpenMeteoCurrent(
    @SerialName("temperature_2m") val temperature: Double = 0.0,
    @SerialName("apparent_temperature") val apparentTemperature: Double = 0.0,
    @SerialName("relative_humidity_2m") val humidity: Int = 0,
    @SerialName("wind_speed_10m") val windSpeed: Double = 0.0,
    @SerialName("weather_code") val weatherCode: Int = 0,
    @SerialName("is_day") val isDay: Int = 1
)
