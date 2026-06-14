package edu.dyds.countries.domain.usecase

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.repository.CountriesRepository

interface SearchCountriesUseCase {
    suspend operator fun invoke(query: String): List<Country>
}

internal class SearchCountriesUseCaseImpl(
    private val repository: CountriesRepository
) : SearchCountriesUseCase {
    override suspend fun invoke(query: String): List<Country> {
        return repository.searchCountries(query)
    }
}
