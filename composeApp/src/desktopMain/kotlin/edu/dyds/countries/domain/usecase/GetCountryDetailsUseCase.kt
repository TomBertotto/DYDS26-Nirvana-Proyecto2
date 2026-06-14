package edu.dyds.countries.domain.usecase

import edu.dyds.countries.domain.entity.Country
import edu.dyds.countries.domain.repository.CountriesRepository

interface GetCountryDetailsUseCase {
    suspend operator fun invoke(id: String): Country?
}

internal class GetCountryDetailsUseCaseImpl(
    private val repository: CountriesRepository
) : GetCountryDetailsUseCase {
    override suspend fun invoke(id: String): Country? {
        return repository.getCountryById(id)
    }
}
