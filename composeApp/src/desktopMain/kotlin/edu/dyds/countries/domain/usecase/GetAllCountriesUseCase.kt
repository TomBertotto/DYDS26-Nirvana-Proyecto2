package edu.dyds.countries.domain.usecase

import edu.dyds.countries.domain.entity.QualifiedCountry
import edu.dyds.countries.domain.qualifier.CountryQualifier
import edu.dyds.countries.domain.repository.CountriesRepository

interface GetAllCountriesUseCase {
    suspend operator fun invoke(): List<QualifiedCountry>
}

internal class GetAllCountriesUseCaseImpl(
    private val repository: CountriesRepository,
    private val qualifier: CountryQualifier
) : GetAllCountriesUseCase {
    override suspend fun invoke(): List<QualifiedCountry> {
        val countries = repository.getAllCountries()
        return qualifier.qualifyCountries(countries)
    }
}
