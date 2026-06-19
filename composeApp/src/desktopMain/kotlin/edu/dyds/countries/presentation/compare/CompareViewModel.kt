package edu.dyds.countries.presentation.compare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.usecase.SearchCountriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CompareViewModel(
    private val searchCountriesUseCase: SearchCountriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompareUiState())
    val uiState: StateFlow<CompareUiState> = _uiState.asStateFlow()

    fun onFirstQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(firstQuery = query)
    }

    fun onSecondQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(secondQuery = query)
    }

    fun searchFirst() {
        val query = _uiState.value.firstQuery
        if (query.isBlank()) return
        performSearch(query, SearchPosition.FIRST)
    }

    fun searchSecond() {
        val query = _uiState.value.secondQuery
        if (query.isBlank()) return
        performSearch(query, SearchPosition.SECOND)
    }

    private fun performSearch(query: String, position: SearchPosition) {
        viewModelScope.launch {
            try {
                updateLoadingState(position, isLoading = true)
                val country = searchCountriesUseCase.invoke(query, DEFAULT_FILTER).firstOrNull()

                if (country != null) {
                    updateCountry(position, country)
                } else {
                    updateError(position, "Country not found")
                }
            } catch (e: Exception) {
                updateError(position, e.message ?: "An error occurred")
            } finally {
                updateLoadingState(position, isLoading = false)
            }
        }
    }

    private fun updateLoadingState(position: SearchPosition, isLoading: Boolean) {
        _uiState.value = when (position) {
            SearchPosition.FIRST -> _uiState.value.copy(isFirstLoading = isLoading)
            SearchPosition.SECOND -> _uiState.value.copy(isSecondLoading = isLoading)
        }
    }

    private fun updateCountry(position: SearchPosition, country: Country) {
        _uiState.value = when (position) {
            SearchPosition.FIRST -> _uiState.value.copy(
                firstCountry = country,
                firstError = null
            )
            SearchPosition.SECOND -> _uiState.value.copy(
                secondCountry = country,
                secondError = null
            )
        }
    }

    private fun updateError(position: SearchPosition, error: String) {
        _uiState.value = when (position) {
            SearchPosition.FIRST -> _uiState.value.copy(firstError = error)
            SearchPosition.SECOND -> _uiState.value.copy(secondError = error)
        }
    }

    companion object {
        private const val DEFAULT_FILTER = "All"
    }
}
