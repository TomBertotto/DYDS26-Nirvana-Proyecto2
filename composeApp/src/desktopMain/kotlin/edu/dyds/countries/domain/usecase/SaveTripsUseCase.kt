package edu.dyds.countries.domain.usecase

import Trip

interface SaveTripsUseCase {
    suspend operator fun invoke(trips: List<Trip>)
}