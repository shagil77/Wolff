package com.shagil.wolff.dependency_injection

import com.shagil.wolff.data.csv.CSVParser
import com.shagil.wolff.data.csv.CompanyListingsParser
import com.shagil.wolff.data.csv.IntradayInfoParser
import com.shagil.wolff.data.repository.StockRepositoryImpl
import com.shagil.wolff.domain.model.CompanyListings
import com.shagil.wolff.domain.model.IntradayInfo
import com.shagil.wolff.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // for abstract functions we don't use @Provides we rather use @Binds
    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingsParser: CompanyListingsParser
    ):CSVParser<CompanyListings>

    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ):CSVParser<IntradayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ):StockRepository
}