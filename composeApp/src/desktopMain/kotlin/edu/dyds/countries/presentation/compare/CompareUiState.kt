package edu.dyds.countries.presentation.compare

import edu.dyds.countries.domain.entity.Country

data class CompareUiState(
    val firstQuery: String = "",
    val secondQuery: String = "",
    val firstCountry: Country? = null,
    val secondCountry: Country? = null,
    val isFirstLoading: Boolean = false,
    val isSecondLoading: Boolean = false,
    val firstError: String? = null,
    val secondError: String? = null
)

enum class SearchPosition {
    FIRST, SECOND
}

