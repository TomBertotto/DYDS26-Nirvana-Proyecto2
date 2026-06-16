package edu.dyds.countries.data.local

import Trip

interface TripsLocalDataSource {

    suspend fun saveTrips(trips: List<Trip>)
    suspend fun getTripById(id: String): Trip?
    suspend fun getAllTrips(): List<Trip>
    suspend fun getTripsByCountryName(countryName: String): List<Trip>
}

class TripsLocalDataSourceImpl : TripsLocalDataSource {
    private val tripCache = mutableMapOf<String, Trip>()

    override suspend fun saveTrips(trips: List<Trip>) {
        tripCache.clear()
        trips.forEach { tripCache[it.id] = it }
    }

    override suspend fun getTripById(id: String): Trip? {
        return tripCache[id]
    }

    override suspend fun getAllTrips(): List<Trip> {
        return tripCache.values.toList()
    }

    override suspend fun getTripsByCountryName(countryName: String): List<Trip> {
        return tripCache.values.filter { it.country.name == countryName }
    }
}