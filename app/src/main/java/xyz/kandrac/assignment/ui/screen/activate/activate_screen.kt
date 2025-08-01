package xyz.kandrac.assignment.ui.screen.activate

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.kandrac.assignment.model.ScratchCard
import xyz.kandrac.assignment.model.ScratchCardProvider
import xyz.kandrac.assignment.ui.OnScreenGoneJobClear
import xyz.kandrac.assignment.ui.ScratchCardViewModel
import xyz.kandrac.assignment.ui.widget.WidgetScratchCard
import javax.inject.Inject

@HiltViewModel
class ScreenActivateViewModel @Inject constructor(
    scratchCardProvider: ScratchCardProvider
): ScratchCardViewModel() {

    val scratchCard = scratchCardProvider.scratchCard

    fun activate() {
        storeJob(viewModelScope.launch {
            (scratchCard.value as? ScratchCard.ScratchedScratchCard)?.activate?.invoke() ?: run {
                throw IllegalStateException("Invalid scratch card state")
            }
        })
    }
}

@Composable
fun ScreenActivate() {
    val viewModel = viewModel<ScreenActivateViewModel>()
    val scratchCard by viewModel.scratchCard.collectAsState()

    OnScreenGoneJobClear(viewModel)

    Column {
        WidgetScratchCard(scratchCard)
        Button(onClick = { viewModel.activate() }, enabled = scratchCard is ScratchCard.ScratchedScratchCard) {
            Text("Activate it!")
        }
    }
}