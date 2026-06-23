package edu.dyds.countries.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.countries.domain.entity.SearchCriteria
import edu.dyds.countries.domain.usecase.SearchCountriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val searchCountriesUseCase: SearchCountriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CountriesUiState())
    val uiState: StateFlow<CountriesUiState> = _uiState.asStateFlow()

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    fun onCriteriaChange(criteria: SearchCriteria) {
        val query = ""
        _uiState.value = _uiState.value.copy(selectedCriteria = criteria, query = query)
    }

    fun loadInitialCountries() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val countries = searchCountriesUseCase(_uiState.value.query, _uiState.value.selectedCriteria)
                _uiState.value = _uiState.value.copy(isLoading = false, countries = countries)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "An error occurred")
            }
        }
    }

    fun search() {
        val query = _uiState.value.query
        val criteria = _uiState.value.selectedCriteria
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val countries = searchCountriesUseCase(query, criteria)
                _uiState.value = if (countries.isEmpty()) {
                    _uiState.value.copy(isLoading = false)
                } else {
                    _uiState.value.copy(isLoading = false, countries = countries)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "An error occurred")
            }
        }
    }
}
