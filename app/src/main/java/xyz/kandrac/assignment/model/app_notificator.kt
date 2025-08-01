package xyz.kandrac.assignment.model

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface AppNotification {
    data object None: AppNotification
    data class Notification(val message: String, val id: Int) : AppNotification
}

class AppNotificator() {

    @VisibleForTesting var currentId = 0
    private val _notification = MutableStateFlow<AppNotification>(AppNotification.None)

    val notification: StateFlow<AppNotification> = _notification

    suspend fun notify(text: String) = _notification.emit(AppNotification.Notification(text, currentId++))

}