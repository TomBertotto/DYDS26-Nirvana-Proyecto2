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

    fun search() {
        val query = _uiState.value.query
        if (query.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val countries = searchCountriesUseCase(query)
            _uiState.value = _uiState.value.copy(countries = countries, isLoading = false)
        }
    }
}
