package edu.dyds.countries.presentation.compare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isFirstLoading = true)
            val country = searchCountriesUseCase.invoke(query,"All").firstOrNull()
            _uiState.value = if (country == null) {
                _uiState.value.copy(isFirstLoading = false)
            } else {
                _uiState.value.copy(firstCountry = country, isFirstLoading = false)
            }
        }
    }

    fun searchSecond() {
        val query = _uiState.value.secondQuery
        if (query.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSecondLoading = true)
            val country = searchCountriesUseCase.invoke(query, "All").firstOrNull()
            _uiState.value = if (country == null) {
                _uiState.value.copy(isSecondLoading = false)
            } else {
                _uiState.value.copy(secondCountry = country, isSecondLoading = false)
            }
        }
    }
}
