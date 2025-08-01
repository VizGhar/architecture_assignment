package xyz.kandrac.assignment

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import xyz.kandrac.assignment.model.AppNotification
import xyz.kandrac.assignment.model.AppNotificator
import xyz.kandrac.assignment.model.ScratchCard
import xyz.kandrac.assignment.model.ScratchCardProvider
import xyz.kandrac.assignment.net.data.ScratchCardVersion
import xyz.kandrac.assignment.net.service.ScratchCardService

@OptIn(ExperimentalCoroutinesApi::class)
class ScratchCardProviderTest {

    private lateinit var service: ScratchCardService
    private lateinit var notificator: AppNotificator
    private lateinit var provider: ScratchCardProvider
    private val testDispatcher = StandardTestDispatcher()

    private val initialId = "b8702305-e3d3-4fac-8fc3-e530924fb17c"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        service = mockk()
        notificator = spyk(AppNotificator())
        provider = ScratchCardProvider(initialId, service, notificator)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `scratch() should change state to ScratchedScratchCard after delay`() = runTest {
        val initial = provider.scratchCard.value
        assertTrue(initial is ScratchCard.UnscratchedScratchCard)

        (initial as ScratchCard.UnscratchedScratchCard).scratch.invoke()
        advanceUntilIdle()

        val new = provider.scratchCard.value
        assertTrue(new is ScratchCard.ScratchedScratchCard)
        assertEquals(initialId, new.id)
    }

    @Test
    fun `activate() should change state to ActivatedScratchCard if version is high enough`() = runTest {
        coEvery { service.sendScratchCardCode(initialId) } returns ScratchCardVersion("278000")

        provider.scratch()
        advanceUntilIdle()

        val scratched = provider.scratchCard.value as ScratchCard.ScratchedScratchCard
        scratched.activate.invoke()

        val result = provider.scratchCard.value
        assertTrue(result is ScratchCard.ActivatedScratchCard)
        assertEquals(initialId, result.id)
    }

    @Test
    fun `activate() should notify user if version is too low`() = runTest {
        coEvery { service.sendScratchCardCode(initialId) } returns ScratchCardVersion("100000")

        provider.scratch()
        advanceUntilIdle()

        val scratched = provider.scratchCard.value as ScratchCard.ScratchedScratchCard
        scratched.activate.invoke()

        assertTrue(provider.scratchCard.value is ScratchCard.ScratchedScratchCard)

        val notification = notificator.notification.value
        assertTrue(notification is AppNotification.Notification)
        assertEquals("Unable to activate card - update your application", (notification as AppNotification.Notification).message)
    }
}