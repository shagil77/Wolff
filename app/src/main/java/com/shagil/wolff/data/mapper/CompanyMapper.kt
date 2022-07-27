package com.shagil.wolff.data.mapper

import com.shagil.wolff.data.local.CompanyListingsEntity
import com.shagil.wolff.data.remote.dto.CompanyInfoDTO
import com.shagil.wolff.domain.model.CompanyInfo
import com.shagil.wolff.domain.model.CompanyListings

fun CompanyListingsEntity.toCompanyListings():CompanyListings {
    return CompanyListings(
        name=name,
        symbol=symbol,
        exchange=exchange
    )
}

fun CompanyListings.toCompanyListingsEntity():CompanyListingsEntity {
    return CompanyListingsEntity(
        name=name,
        symbol=symbol,
        exchange=exchange
    )
}

fun CompanyInfoDTO.toCompanyInfo() : CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}