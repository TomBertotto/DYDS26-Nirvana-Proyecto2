package edu.dyds.countries.domain.usecase

import edu.dyds.countries.domain.entity.Country

interface GetCountryDetailsUseCase {
    suspend operator fun invoke(id: String): Country?
}

