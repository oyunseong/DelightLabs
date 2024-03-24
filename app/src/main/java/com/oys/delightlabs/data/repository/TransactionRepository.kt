package com.oys.delightlabs.data.repository

import com.oys.delightlabs.util.toDate
import java.util.concurrent.TimeUnit

enum class TransactionType {
    All, EXPENSE, INCOME
}

val fakeToday = "2024-06-30T23:59:00Z".toDate()!!.time

class TransactionRepository(
    private val transactionApi: TransactionApi = TransactionApi()
) {

    suspend fun getRecentTransactions(
        size: Int = 20,
        type: TransactionType = TransactionType.All
    ): List<Transaction> {
        val allTransactions = transactionApi.getAllTransactions() //정렬된 데이터라고 가정함.
        val transactions = when (type) {
            TransactionType.All -> allTransactions.takeLast(size)
            TransactionType.EXPENSE -> allTransactions.filter { it.amount < 0 }.takeLast(size)
            TransactionType.INCOME -> allTransactions.filter { it.amount > 0 }.takeLast(size)
        }
        return transactions
    }

    /**
     * 최근 n일 간의 거래내역을 가져온다.
     */
//    suspend fun filterTransaction(day: Long): List<Transaction> {
//        val allTransactions = transactionApi.getAllTransactions()
//        val currentDay = System.currentTimeMillis()
//        val transactions = allTransactions
//            .takeLast(50) //너무 오래 걸려서 임시로 설정
//            .filter {
//                (it.date?.time ?: 0) >= fakeToday - TimeUnit.DAYS.toMillis(day)
//            }
//        return transactions
//    }

    suspend fun filterTransaction(
        day: Long,
        type: TransactionType = TransactionType.INCOME
    ): List<Transaction> {
        val allTransactions = transactionApi.getAllTransactions()
        val currentDay = fakeToday
        val startDate = currentDay - TimeUnit.DAYS.toMillis(day)

        val transactions = mutableListOf<Transaction>()
        for (i in allTransactions.indices.reversed()) {
            val transaction = allTransactions[i]
            val transactionDate = transaction.date?.time ?: 0

            if (transactionDate in startDate..currentDay) {
                when(type) {
                    TransactionType.All -> {}
                    TransactionType.EXPENSE -> if(transaction.amount < 0) transactions.add(transaction)
                    TransactionType.INCOME -> if(transaction.amount > 0) transactions.add(transaction)
                }
            } else {
                break
            }
        }

        return transactions.reversed()
    }
}