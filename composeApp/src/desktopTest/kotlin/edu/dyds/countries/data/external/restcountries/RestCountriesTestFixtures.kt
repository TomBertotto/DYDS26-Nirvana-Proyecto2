package edu.dyds.countries.data.external.restcountries

import io.mockk.coEvery
import io.mockk.mockk

fun createRemoteCountryMock(
    alpha3: String,
    commonName: String,
    officialName: String
): RestCountriesRemoteCountry {
    val remote = mockk<RestCountriesRemoteCountry>()

    coEvery { remote.codes.alpha3 } returns alpha3
    coEvery { remote.names.common } returns commonName
    coEvery { remote.names.official } returns officialName
    coEvery { remote.region } returns "Americas"
    coEvery { remote.subregion } returns "South America"
    coEvery { remote.population } returns 46000000L
    coEvery { remote.area.kilometers } returns 2780400.0

    val currencyMock = mockk<RestCountriesCurrency>()
    coEvery { currencyMock.code } returns "ARS"
    coEvery { currencyMock.name } returns "Argentine peso"
    coEvery { currencyMock.symbol } returns "$"
    coEvery { remote.currencies } returns listOf(currencyMock)

    val languageMock = mockk<RestCountriesLanguage>()
    coEvery { languageMock.name } returns "Spanish"
    coEvery { remote.languages } returns listOf(languageMock)

    coEvery { remote.flag.urlPng } returns "https://flagcdn.com/w320/ar.png"
    coEvery { remote.flag.emoji } returns "🇦🇷"

    val coordinatesMock = mockk<RestCountriesCoordinates>()
    coEvery { coordinatesMock.lat } returns -34.6037
    coEvery { coordinatesMock.lng } returns -58.3816

    val capitalMock = mockk<RestCountriesCapital>()
    coEvery { capitalMock.name } returns "Buenos Aires"
    coEvery { capitalMock.coordinates } returns coordinatesMock

    coEvery { remote.capitals } returns listOf(capitalMock)

    return remote
}
