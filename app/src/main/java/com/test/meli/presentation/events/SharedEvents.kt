package com.test.meli.presentation.events

sealed class SharedEvents {
    object grantedPermimssionCameraEvent : SharedEvents()
    object grantedPermimssionStoreEvent : SharedEvents()
    object grantedPermimssionLocationEvent : SharedEvents()
    object grantedPermimssionAllEvent : SharedEvents()
    object grantedPermimssionCallSuccessEvent : SharedEvents()
}