package edu.dyds.recipes.domain.usecase

import edu.dyds.recipes.domain.entity.WeeklyPlan
import edu.dyds.recipes.domain.repository.WeeklyPlanRepository

class GetWeeklyPlanUseCaseImpl(
    private val repository: WeeklyPlanRepository
) : GetWeeklyPlanUseCase {
    override suspend fun invoke(): WeeklyPlan? = repository.getWeeklyPlan()
}
