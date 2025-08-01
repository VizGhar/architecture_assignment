package xyz.kandrac.assignment

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import xyz.kandrac.assignment.model.AppNotificator
import xyz.kandrac.assignment.model.ScratchCard
import xyz.kandrac.assignment.model.ScratchCardProvider
import xyz.kandrac.assignment.net.data.ScratchCardVersion
import xyz.kandrac.assignment.net.service.ScratchCardService
import xyz.kandrac.assignment.ui.screen.activate.ScreenActivateViewModel
import xyz.kandrac.assignment.ui.screen.scratch.ScreenScratchViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class ScreenScratchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var scratchCardProvider: ScratchCardProvider
    private lateinit var notificator: AppNotificator
    private lateinit var service: ScratchCardService

    private lateinit var scratchViewModel: ScreenScratchViewModel
    private lateinit var activateViewModel: ScreenActivateViewModel

    private val initialId = "b8702305-e3d3-4fac-8fc3-e530924fb17c"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        notificator = AppNotificator()
        service = mockk()

        coEvery { service.sendScratchCardCode(any()) } returns ScratchCardVersion("999999")

        scratchCardProvider = ScratchCardProvider(initialId, service, notificator)

        scratchViewModel = ScreenScratchViewModel(scratchCardProvider)
        activateViewModel = ScreenActivateViewModel(scratchCardProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `scratch() and activate() should move card to proper state`() = testScope.runTest {
        assertTrue(scratchViewModel.scratchCard.value is ScratchCard.UnscratchedScratchCard)

        // scratch test
        scratchViewModel.scratch()
        advanceUntilIdle()
        assertTrue(scratchViewModel.scratchCard.value is ScratchCard.ScratchedScratchCard)
        assertTrue(scratchCardProvider.scratchCard.value is ScratchCard.ScratchedScratchCard)

        // activate test
        activateViewModel.activate()
        advanceUntilIdle()
        assertTrue(activateViewModel.scratchCard.value is ScratchCard.ActivatedScratchCard)
        assertTrue(scratchCardProvider.scratchCard.value is ScratchCard.ActivatedScratchCard)
    }

}
