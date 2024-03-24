package com.oys.delightlabs.data.repository

import com.oys.delightlabs.util.toDate
import com.oys.delightlabs.util.toTime
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Transaction(
    val amount: Float,
    val name: String,
    val timestamp: String,
    val type: String,
) {
    val date: Date?
        get() = timestamp.toDate()

    val time : String?
        get() = timestamp.toTime()
}