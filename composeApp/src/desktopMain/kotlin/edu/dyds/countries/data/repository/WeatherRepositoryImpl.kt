package edu.dyds.countries.data.repository

import edu.dyds.countries.data.external.WeatherExternalSource
import edu.dyds.countries.domain.entity.Weather
import edu.dyds.countries.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val weatherExternalSource: WeatherExternalSource
) : WeatherRepository {
    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): Weather? {
        return runCatching { weatherExternalSource.getCurrentWeather(latitude, longitude) }.getOrNull()
    }
}
