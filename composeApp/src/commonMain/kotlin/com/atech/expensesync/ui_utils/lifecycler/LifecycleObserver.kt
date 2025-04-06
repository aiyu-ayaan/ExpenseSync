package com.atech.expensesync.ui_utils.lifecycler

// Common code
enum class LifeCycle {
    ON_CREATE,
    ON_START,
    ON_RESUME,
    ON_PAUSE,
    ON_STOP,
    ON_DESTROY
}

interface LifecycleObserver {
    fun onStateChanged(state: LifeCycle)
}

class LifecycleRegistry {
    private val observers = mutableListOf<LifecycleObserver>()
    private var currentState: LifeCycle = LifeCycle.ON_CREATE

    fun addObserver(observer: LifecycleObserver) {
        observers.add(observer)
        // Notify the new observer of the current state
        observer.onStateChanged(currentState)
    }

    fun removeObserver(observer: LifecycleObserver) {
        observers.remove(observer)
    }

    fun setLifecycleState(state: LifeCycle) {
        if (currentState != state) {
            currentState = state
            notifyObservers()
        }
    }

    private fun notifyObservers() {
        observers.forEach { it.onStateChanged(currentState) }
    }
}