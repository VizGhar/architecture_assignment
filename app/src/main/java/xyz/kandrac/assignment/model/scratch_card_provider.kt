package xyz.kandrac.assignment.model

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import xyz.kandrac.assignment.net.service.ScratchCardService

sealed class ScratchCard(val id: String, val readableName: String) {
    class UnscratchedScratchCard(id: String, val scratch: suspend () -> Unit) : ScratchCard(id, "Unscratched")
    class ScratchedScratchCard(id: String, val activate: suspend () -> Unit) : ScratchCard(id, "Scratched")
    class ActivatedScratchCard(id: String) : ScratchCard(id, "Activated")
}

class ScratchCardProvider(
    initialCardId: String,
    private val service: ScratchCardService,
    private val notificator: AppNotificator
) {

    private val _scratchCard = MutableStateFlow<ScratchCard>(ScratchCard.UnscratchedScratchCard(initialCardId) { scratch() })

    val scratchCard: StateFlow<ScratchCard> = _scratchCard

    @VisibleForTesting suspend fun scratch() {
        if (scratchCard.value !is ScratchCard.UnscratchedScratchCard) {
            throw IllegalStateException("Can't scratch ")
        }
        delay(2000)
        _scratchCard.emit(ScratchCard.ScratchedScratchCard(_scratchCard.value.id) { activate() })
    }

    @VisibleForTesting suspend fun activate() {
        val version = service.sendScratchCardCode(_scratchCard.value.id)
        if ((version.android.toIntOrNull() ?: 0) > 277028) {
            _scratchCard.emit(ScratchCard.ActivatedScratchCard(_scratchCard.value.id))
        } else {
            notificator.notify("Unable to activate card - update your application")
        }
    }
}