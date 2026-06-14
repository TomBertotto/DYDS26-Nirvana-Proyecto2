package edu.dyds.countries.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.countries.domain.usecase.GetCountryDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getCountryDetailsUseCase: GetCountryDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CountryDetailUiState())
    val uiState: StateFlow<CountryDetailUiState> = _uiState.asStateFlow()

    fun getCountryDetail(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val country = getCountryDetailsUseCase(id)
            _uiState.value = _uiState.value.copy(country = country, isLoading = false)
        }
    }
}
