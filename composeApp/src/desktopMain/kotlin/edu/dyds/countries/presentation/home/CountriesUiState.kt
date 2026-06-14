package edu.dyds.countries.presentation.home

import edu.dyds.countries.domain.entity.QualifiedCountry

data class CountriesUiState(
    val countries: List<QualifiedCountry> = emptyList(),
    val isLoading: Boolean = false
)
