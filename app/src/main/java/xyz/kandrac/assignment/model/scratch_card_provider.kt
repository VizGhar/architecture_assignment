package xyz.kandrac.assignment.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import xyz.kandrac.assignment.net.service.ScratchCardService

sealed class ScratchCard(val id: String) {
    class UnscratchedScratchCard(id: String, val scratch: suspend () -> Unit) : ScratchCard(id)
    class ScratchedScratchCard(id: String, val activate: suspend () -> Unit) : ScratchCard(id)
    class ActivatedScratchCard(id: String) : ScratchCard(id)
}

class ScratchCardProvider(
    initialCardId: String,
    private val service: ScratchCardService,
    private val notificator: AppNotificator
) {

    private val _scratchCard = MutableStateFlow<ScratchCard>(ScratchCard.UnscratchedScratchCard(initialCardId) { scratch() })

    val scratchCard: StateFlow<ScratchCard> = _scratchCard

    private suspend fun scratch() {
        if (scratchCard.value !is ScratchCard.UnscratchedScratchCard) {
            throw IllegalStateException("Can't scratch ")
        }
        delay(2000)
        _scratchCard.emit(ScratchCard.ScratchedScratchCard(_scratchCard.value.id) { activate() })
    }

    private suspend fun activate() {
        val version = service.sendScratchCardCode(_scratchCard.value.id)
        if ((version.android.toIntOrNull() ?: 0) > 277028) {
            _scratchCard.emit(ScratchCard.ActivatedScratchCard(_scratchCard.value.id))
        } else {
            notificator.notify("Unable to activate card - update your application")
        }
    }
}