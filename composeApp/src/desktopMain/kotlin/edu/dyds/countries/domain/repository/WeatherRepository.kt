package edu.dyds.countries.domain.repository

import edu.dyds.countries.domain.entity.Weather

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): Weather?
}
