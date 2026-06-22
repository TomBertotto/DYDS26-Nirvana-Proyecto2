package edu.dyds.countries.presentation.home

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.SearchCriteria

data class CountriesUiState(
    val query: String = "",
    val selectedCriteria: SearchCriteria = SearchCriteria.ALL,
    val countries: List<Country> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

val SearchCriteria.label: String
    get() = when (this) {
        SearchCriteria.ALL -> "All"
        SearchCriteria.NAME -> "Name"
        SearchCriteria.REGION -> "Region"
        SearchCriteria.LANGUAGE -> "Language"
        SearchCriteria.EXACT_NAME -> "Exact Name"
    }

val homeFilterCriteria: List<SearchCriteria> =
    SearchCriteria.entries.filter { it != SearchCriteria.EXACT_NAME }
