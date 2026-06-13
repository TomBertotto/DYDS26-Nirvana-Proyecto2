package edu.dyds.recipes.domain.repository

import edu.dyds.recipes.domain.entity.WeeklyPlan

interface WeeklyPlanRepository {
    suspend fun getWeeklyPlan(): WeeklyPlan?
    suspend fun saveWeeklyPlan(weeklyPlan: WeeklyPlan)
}
