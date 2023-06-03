package com.test.meli.presentation.states

import com.test.meli.data.network.HandledError
import com.test.meli.repository.models.ResultsModel

sealed class ProductStates {
    data class LoadingState(val isVisible: Boolean) : ProductStates()
    data class DataLoadedState(val data: List<ResultsModel>) : ProductStates()
    data class HandledErrorState(val error: HandledError) : ProductStates()
}