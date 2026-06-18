package edu.dyds.countries.presentation.home

import edu.dyds.countries.domain.entity.Country

data class CountriesUiState(
    val query: String = "",
    val selectedCriteria : SearchCriteria = SearchCriteria.ALL,
    val countries: List<Country> = emptyList(),
    val isLoading: Boolean = false
)

enum class SearchCriteria(val displayName: String) {
    ALL("Todos"),
    NAME("Nombre"),
    REGION("Región"),
    LANGUAGE("Idioma")
}
