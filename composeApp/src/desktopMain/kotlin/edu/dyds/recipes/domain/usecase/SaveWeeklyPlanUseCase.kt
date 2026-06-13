package edu.dyds.recipes.domain.usecase

import edu.dyds.recipes.domain.entity.WeeklyPlan

interface SaveWeeklyPlanUseCase {
    suspend operator fun invoke(weeklyPlan: WeeklyPlan)
}
