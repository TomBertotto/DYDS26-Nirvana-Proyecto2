package edu.dyds.countries.data.external.openmeteo

import edu.dyds.countries.data.external.WeatherExternalSource
import edu.dyds.countries.domain.entity.Weather
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

private const val BASE_URL = "https://api.open-meteo.com/v1"
private const val CURRENT_VARIABLES = "temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m,is_day"

class OpenMeteoWeatherExternalSource(
    private val httpClient: HttpClient
) : WeatherExternalSource {

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): Weather? {
        val response: OpenMeteoForecastResponse = httpClient.get("$BASE_URL/forecast") {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("current", CURRENT_VARIABLES)
        }.body()
        return response.current.toDomain()
    }
}
