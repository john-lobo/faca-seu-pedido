package com.jlndev.facaseupedido

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected open val testScheduler: BaseSchedulerProviderTest = SchedulerProviderTest()
    private val liveDataObservers = mutableListOf<Pair<LiveData<*>, Observer<*>>>()

    @Before
    abstract fun setup()

    protected fun <T> observeLiveData(liveData: LiveData<T>, observer: Observer<in T>) {
        liveData.observeForever(observer)
        liveDataObservers.add(Pair(liveData, observer))
    }

    @After
    fun tearDown() {
        for ((liveData, observer) in liveDataObservers) {
            @Suppress("UNCHECKED_CAST")
            val typedObserver = observer as Observer<Any>
            liveData.removeObserver(typedObserver)
        }
        liveDataObservers.clear()
    }
}

