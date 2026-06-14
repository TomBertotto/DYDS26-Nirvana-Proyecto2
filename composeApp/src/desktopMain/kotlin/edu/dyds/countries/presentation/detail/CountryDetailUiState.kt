package edu.dyds.countries.presentation.detail

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.Weather

data class CountryDetailUiState(
    val country: Country? = null,
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val isWeatherLoading: Boolean = false
)
