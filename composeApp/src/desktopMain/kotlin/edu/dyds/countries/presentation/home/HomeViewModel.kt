package edu.dyds.countries.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        _uiState.value = _uiState.value.copy(selectedCriteria = criteria)
    }

    fun loadInitialCountries() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val countries = searchCountriesUseCase.invoke("", _uiState.value.selectedCriteria.displayName)
            _uiState.value = _uiState.value.copy(isLoading = false, countries = countries)
        }
    }

    fun search() {
        val query = _uiState.value.query
        val filter = _uiState.value.selectedCriteria.displayName
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val countries = searchCountriesUseCase.invoke(query, filter)
            _uiState.value = if (countries.isEmpty()) {
                _uiState.value.copy(isLoading = false)
            } else {
                _uiState.value.copy(isLoading = false, countries = countries)
            }
        }
    }
}
