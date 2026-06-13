package edu.dyds.recipes.domain.usecase

import edu.dyds.recipes.domain.entity.WeeklyPlan

interface GetWeeklyPlanUseCase {
    suspend operator fun invoke(): WeeklyPlan?
}
