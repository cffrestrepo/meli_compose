package com.test.meli.usecases

import com.test.meli.commons.Either
import com.test.meli.data.network.HandledError
import com.test.meli.repository.contracts.ProductRepositorySource
import javax.inject.Inject

/***
 * Test class of [FetchRemoteProductsUseCaseTest]
 */
class FetchRemoteProductsUseCase @Inject constructor(private val productRepository: ProductRepositorySource) :
    UseCaseBase<Either<HandledError, Boolean>, String>() {
    override suspend fun execute(params: String): Either<HandledError, Boolean> {
        return productRepository.fetchRemote(params)
    }
}
