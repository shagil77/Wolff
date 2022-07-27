package com.shagil.wolff.domain.repository

import com.shagil.wolff.domain.model.CompanyInfo
import com.shagil.wolff.domain.model.CompanyListings
import com.shagil.wolff.domain.model.IntradayInfo
import com.shagil.wolff.util.Resource
import kotlinx.coroutines.flow.Flow


interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote:Boolean,
        query:String
    ):Flow<Resource<List<CompanyListings>>>

    suspend fun getIntradayInfo(
        symbol:String
    ):Resource<List<IntradayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ):Resource<CompanyInfo>
}
