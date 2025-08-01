package xyz.kandrac.assignment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import xyz.kandrac.assignment.model.AppNotification
import xyz.kandrac.assignment.model.AppNotificator

class AppNotificatorTest {

    private lateinit var notificator: AppNotificator
    private val testDispatcher = StandardTestDispatcher()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        notificator = AppNotificator()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `notify() should emit Notification with correct text and id`() = runTest {
        notificator.notify("Hello")

        val notification = notificator.notification.value as AppNotification.Notification

        assertEquals("Hello", notification.message)
        assertEquals(0, notification.id)
    }

    @Test
    fun `notify() should increment id on multiple notifications`() = runTest {
        notificator.notify("First")
        val firstNotification = notificator.notification.value as AppNotification.Notification

        notificator.notify("Second")
        val secondNotification = notificator.notification.value as AppNotification.Notification

        assertEquals("First", firstNotification.message)
        assertEquals("Second", secondNotification.message)
        assertEquals(0, firstNotification.id)
        assertEquals(1, secondNotification.id)
        assertEquals(2, notificator.currentId)
    }
}