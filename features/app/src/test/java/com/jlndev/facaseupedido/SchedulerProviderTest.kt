package com.jlndev.facaseupedido

import com.jlndev.baseservice.ext.BaseSchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

interface BaseSchedulerProviderTest : BaseSchedulerProvider {
    fun test() : TestScheduler
}

class SchedulerProviderTest : BaseSchedulerProviderTest {
    private val scheduler = TestScheduler()

    override fun computation(): Scheduler {
        return scheduler
    }

    override fun io(): Scheduler {
        return scheduler
    }

    override fun ui(): Scheduler {
        return scheduler
    }

    override fun test(): TestScheduler {
        return scheduler
    }
}