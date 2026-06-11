package edu.dyds.recipes.data.local

import edu.dyds.recipes.domain.entity.WeeklyPlan

interface WeeklyPlanLocalDataSource {
    suspend fun saveWeeklyPlan(weeklyPlan: WeeklyPlan)
    suspend fun getWeeklyPlan(week: Int): WeeklyPlan?
    suspend fun getAllWeeklyPlans(): List<WeeklyPlan>
}

class WeeklyPlanLocalDataSourceImpl : WeeklyPlanLocalDataSource {
    private val weeklyPlanCache = mutableMapOf<Int, WeeklyPlan>()

    override suspend fun saveWeeklyPlan(weeklyPlan: WeeklyPlan) {
        weeklyPlanCache[weeklyPlan.week] = weeklyPlan
    }

    override suspend fun getWeeklyPlan(week: Int): WeeklyPlan? {
        return weeklyPlanCache[week]
    }

    override suspend fun getAllWeeklyPlans(): List<WeeklyPlan> {
        return weeklyPlanCache.values.toList()
    }
}
