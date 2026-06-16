package edu.dyds.countries.domain.usecase

import Trip


interface GetTripsByCountryUseCase {
    suspend operator fun invoke(countryId: String): List<Trip>
}