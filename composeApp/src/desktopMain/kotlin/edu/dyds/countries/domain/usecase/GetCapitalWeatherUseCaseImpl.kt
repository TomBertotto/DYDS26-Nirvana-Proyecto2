package edu.dyds.countries.domain.usecase

import edu.dyds.countries.domain.entity.Weather
import edu.dyds.countries.domain.repository.WeatherRepository

class GetCapitalWeatherUseCaseImpl(
    private val repository: WeatherRepository
) : GetCapitalWeatherUseCase {
    override suspend fun invoke(latitude: Double, longitude: Double): Weather? {
        return repository.getCurrentWeather(latitude, longitude)
    }
}
