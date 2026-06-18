package edu.dyds.countries.domain.usecase

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.repository.CountriesRepository

class SearchCountriesUseCaseImpl(
    private val repository: CountriesRepository
) : SearchCountriesUseCase {
    override suspend fun invoke(query: String, filter : String): List<Country> {
        return repository.searchCountries(query, filter)
    }
}