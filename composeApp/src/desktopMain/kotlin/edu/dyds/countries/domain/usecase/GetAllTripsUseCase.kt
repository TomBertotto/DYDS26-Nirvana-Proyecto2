package edu.dyds.countries.domain.usecase

import Trip

interface GetAllTripsUseCase {
    suspend operator fun invoke(): List<Trip>
}