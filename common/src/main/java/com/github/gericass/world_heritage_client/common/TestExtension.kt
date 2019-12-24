package com.github.gericass.world_heritage_client.common

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.spekframework.spek2.dsl.GroupBody

private object InstantTaskExecutor : TaskExecutor() {
    override fun executeOnDiskIO(runnable: Runnable) {
        runnable.run()
    }

    override fun postToMainThread(runnable: Runnable) {
        runnable.run()
    }

    override fun isMainThread(): Boolean = true
}

fun GroupBody.applyInstantTaskExecutor() {
    beforeEachTest {
        ArchTaskExecutor.getInstance().setDelegate(InstantTaskExecutor)
    }
    afterEachTest {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}

fun GroupBody.applyTestDispatcher() {
    beforeEachTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }
    afterEachTest {
        Dispatchers.resetMain()
    }
}