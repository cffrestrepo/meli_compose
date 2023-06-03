package com.test.meli.presentation.viewmodel

import com.test.meli.presentation.events.SharedEvents
import com.test.meli.presentation.states.SharedStates

class SharedViewModel : BaseViewModel<SharedEvents, SharedStates>() {
    override fun manageEvent(event: SharedEvents) {
        when (event) {
            SharedEvents.grantedPermimssionCameraEvent -> {
                setState(SharedStates.requestPermimssionCameraState)
            }
            SharedEvents.grantedPermimssionLocationEvent -> {
                setState(SharedStates.requestPermimssionLocationState)
            }
            SharedEvents.grantedPermimssionAllEvent -> {
                setState(SharedStates.requestPermimssionAllState)
            }
            SharedEvents.grantedPermimssionStoreEvent -> {
                setState(SharedStates.requestPermimssionStoreState)
            }
            SharedEvents.grantedPermimssionCallSuccessEvent -> {
                setState(SharedStates.requestCallSuccessState)
            }
        }
    }
}