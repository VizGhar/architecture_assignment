package xyz.kandrac.assignment.ui.screen.main

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import xyz.kandrac.assignment.R
import xyz.kandrac.assignment.model.AppNotification
import xyz.kandrac.assignment.model.AppNotificator
import xyz.kandrac.assignment.ui.screen.activate.ScreenActivate
import xyz.kandrac.assignment.ui.screen.detail.ScreenDetail
import xyz.kandrac.assignment.ui.screen.scratch.ScreenScratch
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class ScreenMainViewModel @Inject constructor(): ViewModel() {

    @Inject lateinit var notificator: AppNotificator

    val notification by lazy { notificator.notification }

}

@Composable
fun ScreenMain() {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val viewModel = viewModel<ScreenMainViewModel>()
    val snackState = remember { SnackbarHostState() }

    val notification by viewModel.notification.collectAsState()

    LaunchedEffect(notification) {
        (notification as? AppNotification.Notification)?.let { snackState.showSnackbar(it.message) }
    }

    // simple screen backstack handling - way easier than JetPack Navigation :)
    // sealed class should be even better idea
    var screenBackStack by remember { mutableStateOf(listOf("Main")) }
    val screenName = screenBackStack.last()
    BackHandler(screenBackStack.size > 1) { screenBackStack = screenBackStack.dropLast(1) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackState) },
        topBar = {
            Box(modifier = Modifier.statusBarsPadding().fillMaxWidth()) {
                if (screenName != "Main") { IconButton(onClick = { backDispatcher?.onBackPressed() }) { Icon(painterResource(R.drawable.ic_back), null) } }
                Text(screenName, modifier = Modifier.fillMaxWidth(), fontSize = 40.sp, textAlign = TextAlign.Center)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(screenName, modifier = Modifier.padding(padding)) {
                when (it) {
                    "Main" -> ScreenDetail(
                        onScratchRequired = { screenBackStack += "Scratch" },
                        onActivationRequired = { screenBackStack += "Activate" })
                    "Scratch" -> ScreenScratch()
                    "Activate" -> ScreenActivate()
                }
            }
        }
    }
}
