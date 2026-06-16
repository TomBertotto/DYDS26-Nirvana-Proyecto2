package edu.dyds.countries.domain.usecase

import Trip
import edu.dyds.countries.domain.repository.TripRepository

class GetAllTripsUseCaseImpl(
    private val repository: TripRepository
) : GetAllTripsUseCase {

    override suspend fun invoke(): List<Trip> {
        return repository.getAllTrips()
    }
}