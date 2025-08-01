package xyz.kandrac.assignment.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

// Just to demonstrate I can use interface delegation...

interface JobHandler {
    fun storeJob(job: Job)
    fun clearJob()
}

class JobHandlerImpl: JobHandler {

    var job: Job? = null

    override fun storeJob(job: Job) { this.job = job }

    override fun clearJob() { job?.cancel() }

}

open class ScratchCardViewModel: ViewModel(), JobHandler by JobHandlerImpl()

@Composable
fun OnScreenGoneJobClear(viewModel: ScratchCardViewModel) {
    DisposableEffect(Unit) { onDispose { viewModel.clearJob() } }
}