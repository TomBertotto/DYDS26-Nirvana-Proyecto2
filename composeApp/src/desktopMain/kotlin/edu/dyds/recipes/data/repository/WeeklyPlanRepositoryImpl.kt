package edu.dyds.recipes.data.repository

import edu.dyds.recipes.data.local.WeeklyPlanLocalDataSource
import edu.dyds.recipes.domain.entity.WeeklyPlan
import edu.dyds.recipes.domain.repository.WeeklyPlanRepository

class WeeklyPlanRepositoryImpl(
    private val weeklyPlanLocalDataSource: WeeklyPlanLocalDataSource
) : WeeklyPlanRepository {

    override suspend fun getWeeklyPlan(): WeeklyPlan? = weeklyPlanLocalDataSource.getWeeklyPlan()

    override suspend fun saveWeeklyPlan(weeklyPlan: WeeklyPlan) = weeklyPlanLocalDataSource.saveWeeklyPlan(weeklyPlan)
}
