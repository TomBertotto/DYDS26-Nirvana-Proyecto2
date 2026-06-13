package edu.dyds.recipes.domain.usecase

import edu.dyds.recipes.domain.entity.WeeklyPlan
import edu.dyds.recipes.domain.repository.WeeklyPlanRepository

class SaveWeeklyPlanUseCaseImpl(
    private val repository: WeeklyPlanRepository
) : SaveWeeklyPlanUseCase {
    override suspend fun invoke(weeklyPlan: WeeklyPlan) = repository.saveWeeklyPlan(weeklyPlan)
}
