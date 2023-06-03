package com.test.meli.data.remote

import com.test.meli.commons.Either
import com.test.meli.data.network.ErrorFactory
import com.test.meli.data.network.HandledError
import com.test.meli.data.remote.response.LookFor
import com.test.meli.data.remote.sources.ProductDataRemoteSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/***
 * Test class [ProductDataRemoteImplTest]
 */
class ProductDataRemoteImpl @Inject constructor(
    private val retrofitServicesInterface: RetrofitServicesInterface,
    private val errorFactory: ErrorFactory
) :
    ProductDataRemoteSource {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getProductsBySearch(query: String): Either<HandledError, LookFor?> {
        return suspendCancellableCoroutine { continuation ->
            val call: Call<LookFor>? =
                retrofitServicesInterface.getProductsBySearch(query)

            call?.enqueue(object : Callback<LookFor> {
                override fun onFailure(call: Call<LookFor>, throwable: Throwable) {
                    val handledError: HandledError = errorFactory.handledError(throwable)
                    continuation.resume(Either.Left(handledError), onCancellation = { })
                }

                override fun onResponse(call: Call<LookFor>, response: Response<LookFor>) {
                    continuation.resume(Either.Right(response.body()), onCancellation = { })
                }
            })
        }
    }
}
