package com.test.meli.data.remote.sources

import com.test.meli.commons.Either
import com.test.meli.data.network.HandledError
import com.test.meli.data.remote.response.LookFor

interface ProductDataRemoteSource {

    suspend fun getProductsBySearch(query: String): Either<HandledError, LookFor?>
}
