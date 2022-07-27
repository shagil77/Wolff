package com.shagil.wolff.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(
        companyListingsEntities: List<CompanyListingsEntity>
    )

    @Query("DELETE FROM companylistingsentity")
    suspend fun clearCompanyListings()

    @Query(
        """
            SELECT *
            FROM companylistingsentity
            WHERE LOWER(name) LIKE "%" || LOWER(:query) || "%" OR 
                UPPER(:query) == symbol 
        """
    )
    suspend fun searchCompanyListings(query: String) : List<CompanyListingsEntity>
}