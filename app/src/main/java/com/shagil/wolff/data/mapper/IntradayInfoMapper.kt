package com.shagil.wolff.data.mapper

import com.shagil.wolff.data.remote.dto.IntradayInfoDTO
import com.shagil.wolff.domain.model.IntradayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun IntradayInfoDTO.toIntradayInfo() : IntradayInfo {
    val pattern="yyyy-MM-dd HH:mm:ss";
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timestamp, formatter)
    return IntradayInfo(
        date = localDateTime,
        close = close
    )
}