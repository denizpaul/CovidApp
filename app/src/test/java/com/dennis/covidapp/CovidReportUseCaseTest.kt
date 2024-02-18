package com.dennis.covidapp

import com.dennis.covidapp.data.api.CovidApi
import com.dennis.covidapp.data.repository.CovidRepository
import com.dennis.covidapp.domain.models.CovidApiResponse
import com.dennis.covidapp.domain.models.CovidData
import com.dennis.covidapp.domain.models.RegionData
import com.dennis.covidapp.domain.models.RegionsResponse
import com.dennis.covidapp.domain.usecases.CovidReportUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class CovidReportUseCaseTest {

    @Mock
    private lateinit var mockCovidApi: CovidApi

    private lateinit var covidReportUseCase: CovidReportUseCase

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        covidReportUseCase = CovidReportUseCase(CovidRepository(mockCovidApi))
    }

    @Test
    fun testGetGlobalStatistics() = runBlocking {
        val mockResponse = CovidApiResponse(covidData = CovidData(deaths = 10))
        val mockedApiResponse = Response.success(mockResponse)
        `when`(mockCovidApi.getGlobalStatistics()).thenReturn(mockedApiResponse)
        val response = covidReportUseCase.getGlobalStatistics()

        assertNotNull(response.body())
        assertEquals(10, response.body()?.covidData?.deaths)
    }

    @Test
    fun testGetRegions() = runBlocking {
        val mockResponse = createMockRegionsResponse()
        val mockedApiResponse = Response.success(mockResponse)
        `when`(mockCovidApi.getRegions()).thenReturn(mockedApiResponse)
        val response = covidReportUseCase.getRegions()

        assertEquals(2, mockResponse.data.size)
        assertEquals("Argentina", mockResponse.data[0].name)
        assertEquals("ARG", mockResponse.data[0].iso)
        assertEquals("Australia", mockResponse.data[1].name)
        assertEquals("AUS", mockResponse.data[1].iso)
    }

    private fun createMockRegionsResponse(): RegionsResponse {
        val regionDataList = ArrayList<RegionData>().apply {
            add(RegionData("ARG", "Argentina"))
            add(RegionData("AUS", "Australia"))
        }
        return RegionsResponse(regionDataList)
    }

    @Test
    fun testGetRegionSpecificReport() = runBlocking {
        val regionCode = "US"
        val mockResponse = CovidApiResponse(covidData = CovidData(deaths = 20))
        val mockedApiResponse = Response.success(mockResponse)
        `when`(mockCovidApi.getRegionSpecificReport(regionCode)).thenReturn(mockedApiResponse)

        val response = covidReportUseCase.getRegionSpecificReport(regionCode)

        assertTrue(response.isSuccessful)
        assertNotNull(response.body())
        assertEquals(20, response.body()?.covidData?.deaths)
    }

}
