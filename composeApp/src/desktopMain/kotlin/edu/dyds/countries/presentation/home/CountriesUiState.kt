package edu.dyds.countries.presentation.home

import edu.dyds.countries.domain.entity.Country

data class CountriesUiState(
    val query: String = "",
    val countries: List<Country> = emptyList(),
    val isLoading: Boolean = false
)
