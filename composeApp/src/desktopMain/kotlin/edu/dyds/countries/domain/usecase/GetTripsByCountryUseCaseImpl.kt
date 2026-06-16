package edu.dyds.countries.domain.usecase

import Trip
import edu.dyds.countries.domain.repository.TripRepository

class GetTripsByCountryUseCaseImpl(
    private val repository: TripRepository
) : GetTripsByCountryUseCase {

    override suspend fun invoke(countryId: String): List<Trip> {
        return repository.getTripsByCountryName(countryId)
    }
}