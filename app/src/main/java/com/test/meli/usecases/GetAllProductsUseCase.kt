package com.test.meli.usecases

import com.test.meli.commons.Either
import com.test.meli.data.network.HandledError
import com.test.meli.repository.contracts.ProductRepositorySource
import com.test.meli.repository.models.ResultsModel
import javax.inject.Inject

/***
 * Test class [GetAllProductsUseCaseTest]
 */
class GetAllProductsUseCase @Inject constructor(private val productRepository: ProductRepositorySource) :
    UseCaseBase<Either<HandledError, List<ResultsModel>>, Unit>() {
    override suspend fun execute(params: Unit): Either<HandledError, List<ResultsModel>> {
        return productRepository.getAll()
    }
}
