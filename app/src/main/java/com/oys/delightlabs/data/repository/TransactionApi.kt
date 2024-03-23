package com.oys.delightlabs.data.repository

import android.content.Context
import android.util.Log
import com.oys.delightlabs.DelightLabsApp
import com.oys.delightlabs.R
import com.oys.delightlabs.util.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import java.util.concurrent.atomic.AtomicReference

class TransactionApi(
    private val context: Context = DelightLabsApp.context
) {
    private var allTransaction = AtomicReference<List<Transaction>>(emptyList())

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun getAllTransactions(): List<Transaction> {
        return withContext(Dispatchers.Default) {
            if (allTransaction.get().isNotEmpty()) {
                return@withContext allTransaction.get()
            }
            val inputStream = context.resources.openRawResource(R.raw.jsonformatter)
            val transactions = json.decodeFromStream<List<Transaction>>(inputStream)
            allTransaction.set(transactions)
            Log.d("TransactionRepository", "allTransaction.size: ${transactions.size}")
            return@withContext transactions
        }
    }
}