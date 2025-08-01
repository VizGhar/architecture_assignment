package xyz.kandrac.assignment.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface AppNotification {
    data object None: AppNotification
    data class Notification(val message: String) : AppNotification
}

class AppNotificator() {

    private val _notification = MutableStateFlow<AppNotification>(AppNotification.None)

    val notification: StateFlow<AppNotification> = _notification

    suspend fun notify(notification: AppNotification) = _notification.emit(notification)

}