package edu.dyds.countries.presentation.home

import edu.dyds.countries.domain.entity.Country

data class CountriesUiState(
    val query: String = "",
    val selectedCriteria : SearchCriteria = SearchCriteria.ALL,
    val countries: List<Country> = emptyList(),
    val isLoading: Boolean = false
)

enum class SearchCriteria(val displayName: String) {
    ALL("All"),
    NAME("Name"),
    REGION("Region"),
    LANGUAGE("Language")
}
