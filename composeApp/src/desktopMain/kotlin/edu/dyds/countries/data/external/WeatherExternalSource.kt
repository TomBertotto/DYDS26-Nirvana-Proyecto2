package edu.dyds.countries.data.external

import edu.dyds.countries.domain.entity.Weather

interface WeatherExternalSource {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): Weather?
}
