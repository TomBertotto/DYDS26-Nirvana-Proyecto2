package edu.dyds.countries.domain.usecase

import edu.dyds.countries.domain.entity.Country

interface SearchCountriesUseCase {
    suspend operator fun invoke(query: String, filter: String): List<Country>
}

