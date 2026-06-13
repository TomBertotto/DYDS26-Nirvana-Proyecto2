package edu.dyds.recipes.data.local

import edu.dyds.recipes.domain.entity.WeeklyPlan

interface WeeklyPlanLocalDataSource {
    suspend fun saveWeeklyPlan(weeklyPlan: WeeklyPlan)
    suspend fun getWeeklyPlan(): WeeklyPlan?
}
