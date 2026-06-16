package edu.dyds.countries.data.repository

import Trip
import edu.dyds.countries.data.local.TripsLocalDataSource
import edu.dyds.countries.domain.repository.TripRepository

class TripRepositoryImpl(
    private val localDataSource: TripsLocalDataSource
) : TripRepository {
    override suspend fun saveTrips(trips: List<Trip>) {
        localDataSource.saveTrips(trips)
    }

    override suspend fun getTripById(id: String): Trip? {
        return localDataSource.getTripById(id)
    }

    override suspend fun getAllTrips(): List<Trip> {
        return localDataSource.getAllTrips()
    }

    override suspend fun getTripsByCountryName(countryName: String): List<Trip> {
        return localDataSource.getTripsByCountryName(countryName)
    }

}
