package com.shagil.wolff.domain.model

data class CompanyListings(
    val name:String,
    val symbol:String,
    val exchange:String
)

// we use CompanyMapper.kt to map CompanyListingsEntity.kt to CompanyListings.kt
