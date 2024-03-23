package com.oys.delightlabs.data.repository

import android.content.Context
import android.util.Log
import com.oys.delightlabs.DelightLabsApp
import com.oys.delightlabs.R
import com.oys.delightlabs.extension.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import java.util.Date
import java.util.concurrent.TimeUnit

class TransactionRepository(
    private val context: Context = DelightLabsApp.context
) {

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun getAllTransactions(): List<Transaction> {
        return withContext(Dispatchers.Default) {
            val inputStream = context.resources.openRawResource(R.raw.jsonformatter)
            val transactions = json.decodeFromStream<List<Transaction>>(inputStream)
            Log.d("TransactionRepository", transactions.size.toString() + " transactions=${transactions.firstOrNull()}")
            return@withContext transactions
        }
    }

    suspend fun getTransactions(
        day: Long,
        size: Int,
    ): List<Transaction> {
        val allTransactions = getAllTransactions()
        val transactions  = allTransactions
            .takeLast(size)
            .filter { it.date?.inDay(day) == true }

        return transactions.also {
            Log.d("TransactionRepository", "getTransactions($day): $transactions")
        }
    }

    private fun Date.inDay(day:  Long): Boolean {
        return time > System.currentTimeMillis() - TimeUnit.DAYS.toMillis(day)
    }
}