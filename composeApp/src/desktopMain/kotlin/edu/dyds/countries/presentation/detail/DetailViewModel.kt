package edu.dyds.countries.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.countries.domain.usecase.GetCapitalWeatherUseCase
import edu.dyds.countries.domain.usecase.GetCountryDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getCountryDetailsUseCase: GetCountryDetailsUseCase,
    private val getCapitalWeatherUseCase: GetCapitalWeatherUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CountryDetailUiState())
    val uiState: StateFlow<CountryDetailUiState> = _uiState.asStateFlow()

    fun getCountryDetail(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, weather = null)
            val country = getCountryDetailsUseCase(id)
            _uiState.value = _uiState.value.copy(country = country, isLoading = false)

            val latitude = country?.capitalLatitude
            val longitude = country?.capitalLongitude
            if (latitude != null && longitude != null) {
                _uiState.value = _uiState.value.copy(isWeatherLoading = true)
                val weather = getCapitalWeatherUseCase(latitude, longitude)
                _uiState.value = _uiState.value.copy(weather = weather, isWeatherLoading = false)
            }
        }
    }
}
