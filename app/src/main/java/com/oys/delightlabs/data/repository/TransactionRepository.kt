package com.oys.delightlabs.data.repository


class TransactionRepository(
    private val transactionService: TransactionService = TransactionService()
) {

    fun filterTransaction(): List<Transaction> {
        return transactionService.getAllTransaction().subList(3, 10)
    }
}