package edu.dyds.countries.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.countries.data.external.CountryExternalSourceBroker
import edu.dyds.countries.data.external.openmeteo.OpenMeteoExternalSource
import edu.dyds.countries.data.external.restcountries.RestCountriesExternalSource
import edu.dyds.countries.data.local.CountriesLocalDataSourceImpl
import edu.dyds.countries.data.repository.CountriesRepositoryImpl
import edu.dyds.countries.domain.qualifier.CountryQualifier
import edu.dyds.countries.domain.usecase.GetAllCountriesUseCaseImpl
import edu.dyds.countries.domain.usecase.GetCountryDetailsUseCaseImpl
import edu.dyds.countries.presentation.detail.DetailViewModel
import edu.dyds.countries.presentation.home.HomeViewModel

object CountriesDependencyInjector {

    private val restCountriesDataSource = RestCountriesExternalSource()
    private val openMeteoDataSource = OpenMeteoExternalSource()

    private val countryExternalSourceBroker = CountryExternalSourceBroker(
        openMeteoSource = openMeteoDataSource,
        restCountriesSource = restCountriesDataSource
    )

    private val localDataSource = CountriesLocalDataSourceImpl()
    private val countryQualifier = CountryQualifier()

    private val countriesRepository = CountriesRepositoryImpl(
        countryDetailExternalSource = countryExternalSourceBroker,
        allCountriesExternalSource = restCountriesDataSource,
        localDataSource = localDataSource
    )

    private val getCountryDetailsUseCase = GetCountryDetailsUseCaseImpl(countriesRepository)
    private val getAllCountriesUseCase = GetAllCountriesUseCaseImpl(countriesRepository, countryQualifier)

    @Composable
    fun getDetailViewModel(): DetailViewModel {
        return viewModel { DetailViewModel(getCountryDetailsUseCase) }
    }

    @Composable
    fun getHomeViewModel(): HomeViewModel {
        return viewModel { HomeViewModel(getAllCountriesUseCase) }
    }
}
