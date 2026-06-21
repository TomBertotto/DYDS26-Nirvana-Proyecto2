package edu.dyds.countries.domain.usecase

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.entity.SearchCriteria

interface SearchCountriesUseCase {
    suspend operator fun invoke(query: String, criteria: SearchCriteria): List<Country>
}
