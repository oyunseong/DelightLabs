package com.oys.delightlabs.ui.screen.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oys.delightlabs.R
import com.oys.delightlabs.ui.component.ToolBar
import com.oys.delightlabs.ui.component.VerticalSpacer
import com.oys.delightlabs.ui.screen.transaction.graph.GraphScreen
import com.oys.delightlabs.ui.theme.Gray100
import com.oys.delightlabs.ui.theme.Gray650
import com.oys.delightlabs.ui.theme.body3
import com.oys.delightlabs.ui.theme.mainColor
import kotlinx.coroutines.delay

@Composable
fun TransactionsScreen(
    viewModel: TransactionScreenViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit, block = {
//        delay(2000)
//        viewModel.updateUiState {
//            copy(test = "test1")
//        }
    })

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        ToolBar(
            title = stringResource(id = R.string.transaction),
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState())
        ) {
            VerticalSpacer(dp = 2.dp)
            CategoryButton()
//            GraphScreen(
//                graphModel = viewModel.getGraphModel()
//            )
            RecentTransactions()
        }
    }
}

// TODO isClick
@Composable
fun CategoryButton() {
    Box(
        modifier = Modifier
            .clip(
                shape = RoundedCornerShape(20.dp)
            )
            .background(color = Gray100)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = mainColor)
                    .padding(horizontal = 17.dp, vertical = 5.dp),
                text = "Week",
                style = body3,
                color = Color.White
            )
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Color.Transparent)
                    .padding(horizontal = 17.dp, vertical = 5.dp),
                text = "Month",
                color = Gray650
            )
        }
    }

}