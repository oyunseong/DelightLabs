package com.oys.delightlabs.data.repository

import android.content.Context
import android.util.Log
import com.oys.delightlabs.DelightLabsApp
import com.oys.delightlabs.R
import com.oys.delightlabs.extension.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream

class TransactionService(
    private val context: Context = DelightLabsApp.context
) {
    @OptIn(ExperimentalSerializationApi::class)
    fun getAllTransaction(): List<Transaction> {
        val inputStream = context.resources.openRawResource(R.raw.jsonformatter)
        val transactions = json.decodeFromStream<List<Transaction>>(inputStream)
        Log.d("TransactionRepository", transactions.toString())
        return transactions
    }
}