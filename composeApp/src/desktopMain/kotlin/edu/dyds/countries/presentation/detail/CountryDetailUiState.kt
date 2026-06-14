package edu.dyds.countries.presentation.detail

import edu.dyds.countries.domain.entity.Country

data class CountryDetailUiState(
    val country: Country? = null,
    val isLoading: Boolean = false
)
