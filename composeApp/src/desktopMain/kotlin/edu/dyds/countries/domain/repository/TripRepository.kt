package edu.dyds.countries.domain.repository

import Trip

interface TripRepository {
    suspend fun getAllTrips(): List<Trip>

    suspend fun saveTrips(trips: List<Trip>)
    suspend fun getTripById(id: String): Trip?
    suspend fun getTripsByCountryName(countryName: String): List<Trip>
}