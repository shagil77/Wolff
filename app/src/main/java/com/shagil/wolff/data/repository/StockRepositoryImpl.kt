package com.shagil.wolff.data.repository

import com.shagil.wolff.data.csv.CSVParser
import com.shagil.wolff.data.csv.CompanyListingsParser
import com.shagil.wolff.data.local.StockDatabase
import com.shagil.wolff.data.mapper.toCompanyInfo
import com.shagil.wolff.data.mapper.toCompanyListings
import com.shagil.wolff.data.mapper.toCompanyListingsEntity
import com.shagil.wolff.data.remote.StockApi
import com.shagil.wolff.domain.model.CompanyInfo
import com.shagil.wolff.domain.model.CompanyListings
import com.shagil.wolff.domain.model.IntradayInfo
import com.shagil.wolff.domain.repository.StockRepository
import com.shagil.wolff.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api:StockApi,
    private val db:StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListings>,
    private val intradayInfoParser: CSVParser<IntradayInfo>

):StockRepository {
    private val dao=db.stockDao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListings>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings=dao.searchCompanyListings(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListings() }
            ))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if(shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListings = try {
                val response = api.getListings()
                // we should not parse the csv data here, as it will violate the Single Responsibility of StockRepositoryImpl.kt
                // which is only to fetch the data. We create a separate file in csv package for that

                companyListingsParser.parse(response.byteStream())
            } catch (e:IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't find data!"))
                null
            } catch (e:HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't find data!"))
                null
            }

            remoteListings?.let { listings->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListingsEntity() }
                )
                emit(Resource.Success(
                    data = dao
                        .searchCompanyListings("")
                        .map { it.toCompanyListings() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val results = intradayInfoParser.parse(response.byteStream())
            Resource.Success(results)
        } catch (e:IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )
        } catch (e:HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())
        } catch (e:IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info!"
            )
        } catch (e:HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info!"
            )
        }
    }
}