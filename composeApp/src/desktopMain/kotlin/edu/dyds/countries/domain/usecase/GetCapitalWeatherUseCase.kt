package edu.dyds.countries.domain.usecase

import edu.dyds.countries.domain.entity.Weather

interface GetCapitalWeatherUseCase {
    suspend operator fun invoke(latitude: Double, longitude: Double): Weather?
}
