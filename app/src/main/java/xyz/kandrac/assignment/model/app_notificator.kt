package xyz.kandrac.assignment.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface AppNotification {
    data object None: AppNotification
    data class Notification(val message: String, private val id: Int) : AppNotification
}

class AppNotificator() {

    private var currentId = 0
    private val _notification = MutableStateFlow<AppNotification>(AppNotification.None)

    val notification: StateFlow<AppNotification> = _notification

    suspend fun notify(text: String) = _notification.emit(AppNotification.Notification(text, currentId++))

}