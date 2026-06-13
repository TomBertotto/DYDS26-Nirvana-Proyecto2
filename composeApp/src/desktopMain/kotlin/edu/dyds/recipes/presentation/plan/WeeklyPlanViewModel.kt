package edu.dyds.recipes.presentation.plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.recipes.domain.usecase.GetWeeklyPlanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeeklyPlanViewModel(
    private val getWeeklyPlanUseCase: GetWeeklyPlanUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeeklyPlanUiState())
    val uiState: StateFlow<WeeklyPlanUiState> = _uiState.asStateFlow()

    init {
        loadWeeklyPlan()
    }

    fun loadWeeklyPlan() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val weeklyPlan = getWeeklyPlanUseCase()
            _uiState.value = _uiState.value.copy(
                recipes = weeklyPlan?.recipes?.values?.toList() ?: emptyList(),
                isLoading = false
            )
        }
    }
}
