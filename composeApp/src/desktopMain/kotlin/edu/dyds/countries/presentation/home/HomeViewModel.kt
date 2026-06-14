package edu.dyds.countries.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.countries.domain.usecase.GetAllCountriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getAllCountriesUseCase: GetAllCountriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CountriesUiState())
    val uiState: StateFlow<CountriesUiState> = _uiState.asStateFlow()

    init {
        getAllCountries()
    }

    fun getAllCountries() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val countries = getAllCountriesUseCase()
            _uiState.value = _uiState.value.copy(countries = countries, isLoading = false)
        }
    }
}
