package com.shagil.wolff.presentation.company_listings

import com.shagil.wolff.domain.model.CompanyListings

data class CompanyListingsState(
    val companies:List<CompanyListings> = emptyList(),
    val isLoading:Boolean = false,
    val isRefreshing:Boolean=false,
    val searchQuery:String=""
)
