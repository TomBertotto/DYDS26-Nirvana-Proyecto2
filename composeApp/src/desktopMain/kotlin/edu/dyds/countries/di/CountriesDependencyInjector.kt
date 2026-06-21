package edu.dyds.countries.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.countries.data.external.openmeteo.OpenMeteoWeatherExternalSource
import edu.dyds.countries.data.external.openmeteo.WeatherExternalSourceImpl
import edu.dyds.countries.data.external.restcountries.CountriesListExternalSourceImpl
import edu.dyds.countries.data.external.restcountries.CountryDetailExternalSourceImpl
import edu.dyds.countries.data.external.restcountries.RestCountriesExternalSource
import edu.dyds.countries.data.local.CountriesLocalDataSourceImpl
import edu.dyds.countries.data.repository.CountriesRepositoryImpl
import edu.dyds.countries.data.repository.WeatherRepositoryImpl
import edu.dyds.countries.domain.usecase.GetCapitalWeatherUseCaseImpl
import edu.dyds.countries.domain.usecase.GetCountryDetailsUseCaseImpl
import edu.dyds.countries.domain.usecase.SearchCountriesUseCaseImpl
import edu.dyds.countries.presentation.compare.CompareViewModel
import edu.dyds.countries.presentation.detail.DetailViewModel
import edu.dyds.countries.presentation.home.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object CountriesDependencyInjector {

    private val httpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val restCountriesExternalSource = RestCountriesExternalSource(httpClient)
    private val openMeteoWeatherExternalSource = OpenMeteoWeatherExternalSource(httpClient)

    private val countriesListExternalSource = CountriesListExternalSourceImpl(restCountriesExternalSource)
    private val countryDetailExternalSource = CountryDetailExternalSourceImpl(restCountriesExternalSource)
    private val weatherExternalSource = WeatherExternalSourceImpl(openMeteoWeatherExternalSource)

    private val localDataSource = CountriesLocalDataSourceImpl()

    private val countriesRepository = CountriesRepositoryImpl(
        countriesListExternalSource = countriesListExternalSource,
        countryDetailExternalSource = countryDetailExternalSource,
        localDataSource = localDataSource
    )

    private val weatherRepository = WeatherRepositoryImpl(weatherExternalSource)

    private val searchCountriesUseCase = SearchCountriesUseCaseImpl(countriesRepository)
    private val getCountryDetailsUseCase = GetCountryDetailsUseCaseImpl(countriesRepository)
    private val getCapitalWeatherUseCase = GetCapitalWeatherUseCaseImpl(weatherRepository)

    @Composable
    fun getDetailViewModel(): DetailViewModel {
        return viewModel { DetailViewModel(getCountryDetailsUseCase, getCapitalWeatherUseCase) }
    }

    @Composable
    fun getHomeViewModel(): HomeViewModel {
        return viewModel { HomeViewModel(searchCountriesUseCase) }
    }

    @Composable
    fun getCompareViewModel(): CompareViewModel {
        return viewModel { CompareViewModel(searchCountriesUseCase) }
    }
}
