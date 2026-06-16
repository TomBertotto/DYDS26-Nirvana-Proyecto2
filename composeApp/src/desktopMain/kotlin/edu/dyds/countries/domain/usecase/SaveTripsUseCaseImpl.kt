package edu.dyds.countries.domain.usecase

import Trip
import edu.dyds.countries.domain.repository.TripRepository

class SaveTripsUseCaseImpl(
    private val repository: TripRepository
) : SaveTripsUseCase {

    override suspend fun invoke(trips: List<Trip>) {
        repository.saveTrips(trips)
    }
}