package edu.dyds.countries.domain.usecase

import Trip
import edu.dyds.countries.domain.repository.TripRepository

class GetTripByIdUseCaseImpl(
    private val repository: TripRepository
) : GetTripByIdUseCase {

    override suspend fun invoke(id: String): Trip? {
        return repository.getTripById(id)
    }
}