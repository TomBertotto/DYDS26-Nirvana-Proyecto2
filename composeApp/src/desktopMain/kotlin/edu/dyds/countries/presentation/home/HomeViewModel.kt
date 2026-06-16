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
        viewModelScope.launch {
            _uiState.emit(
                CountriesUiState(isLoading = true)
            )

            val countries = searchCountriesUseCase.invoke(query)

            _uiState.emit(
                CountriesUiState(
                    isLoading = false,
                    countries = countries
                )
            )
        }
    }
}
