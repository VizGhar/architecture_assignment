package xyz.kandrac.assignment.ui.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import xyz.kandrac.assignment.model.ScratchCard
import xyz.kandrac.assignment.model.ScratchCardProvider
import xyz.kandrac.assignment.ui.widget.WidgetScratchCard
import javax.inject.Inject

@HiltViewModel
class ScreenDetailViewModel @Inject constructor(
    scratchCardProvider: ScratchCardProvider
): ViewModel() {

    val scratchCard = scratchCardProvider.scratchCard

}

@Composable
fun ScreenDetail(
    onScratchRequired: () -> Unit,
    onActivationRequired: () -> Unit,
) {
    val viewModel = viewModel<ScreenDetailViewModel>()
    val scratchCard by viewModel.scratchCard.collectAsState()

    Column {
        WidgetScratchCard(scratchCard)
        Row {
            Button(onClick = onScratchRequired, enabled = scratchCard is ScratchCard.UnscratchedScratchCard) {
                Text("Scratch it!")
            }
            Button(onClick = onActivationRequired, enabled = scratchCard is ScratchCard.ScratchedScratchCard) {
                Text("Activate it!")
            }
        }
    }
}