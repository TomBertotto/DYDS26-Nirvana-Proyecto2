package edu.dyds.countries.domain.usecase

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.SearchCriteria
import edu.dyds.countries.domain.repository.CountriesRepository

class SearchCountriesUseCaseImpl(
    private val repository: CountriesRepository
) : SearchCountriesUseCase {

    override suspend fun invoke(query: String, criteria: SearchCriteria): List<Country> {
        val countries = repository.getAllCountries()
        return filterCountries(countries, query, criteria)
    }

    private fun filterCountries(
        countries: List<Country>,
        query: String,
        criteria: SearchCriteria
    ): List<Country> = when (criteria) {
        SearchCriteria.ALL -> countries
        SearchCriteria.NAME -> countries.filter { it.name.contains(query, ignoreCase = true) }
        SearchCriteria.EXACT_NAME -> countries.filter { it.name.equals(query, ignoreCase = true) }
        SearchCriteria.REGION -> countries.filter { it.region.contains(query, ignoreCase = true) }
        SearchCriteria.LANGUAGE -> countries.filter { country ->
            country.languages.any { it.contains(query, ignoreCase = true) }
        }
    }
}
