package com.oys.delightlabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.oys.delightlabs.data.repository.TransactionRepository
import com.oys.delightlabs.ui.screen.transaction.TransactionsScreen
import com.oys.delightlabs.ui.screen.transaction.graph.GraphModel
import com.oys.delightlabs.ui.theme.DelightLabsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DelightLabsTheme {
                // TODO 바텀네비
                TransactionsScreen()
            }
        }
    }
}