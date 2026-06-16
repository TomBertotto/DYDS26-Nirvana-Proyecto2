package edu.dyds.countries.domain.usecase

import Trip

interface GetTripByIdUseCase {
    suspend operator fun invoke(id: String): Trip?
}