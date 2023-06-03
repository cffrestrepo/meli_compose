package com.test.meli.repository

import com.test.meli.commons.Constants.Companion.UNEXPECTED
import com.test.meli.commons.Constants.Companion.UNEXPECTED_MESSAGE
import com.test.meli.commons.Constants.Companion.UNKNOWN
import com.test.meli.commons.Constants.Companion.UNKNOWN_MESSAGE_SERVER
import com.test.meli.commons.Either
import com.test.meli.data.local.dao.ProductDao
import com.test.meli.data.local.entities.ProductEntity
import com.test.meli.data.network.HandledError
import com.test.meli.data.remote.sources.ProductDataRemoteSource
import com.test.meli.repository.contracts.ProductRepositorySource
import com.test.meli.repository.mappers.ProductMapper
import com.test.meli.repository.models.ResultsModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/***
 * Test class [ProductRepositoryImplTest]
 */
class ProductRepositoryImpl @Inject constructor(
    private val productSource: ProductDao,
    private val productDataRemoteSource: ProductDataRemoteSource,
    private val productMapper: ProductMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    ProductRepositorySource {
    override suspend fun getAll(): Either<HandledError, List<ResultsModel>> {
        return withContext(dispatcher) {
            try {
                val productsEntity = productSource.getAll()
                val productsModel = productMapper.productEntityToProductModel(productsEntity)
                Either.Right(productsModel)
            } catch (ex: java.lang.Exception) {
                Either.Left(
                    HandledError.UnExpected(
                        ex.message ?: UNEXPECTED_MESSAGE,
                        code = UNEXPECTED
                    )
                )
            }
        }
    }

    override suspend fun fetchRemote(query: String): Either<HandledError, Boolean> {
        return withContext(dispatcher) {
            val result = productDataRemoteSource.getProductsBySearch(query)

            result.foldSuspendable(
                functionLeft = {
                    Either.Left(it)
                }, functionRight = {
                    it?.let { lookFor ->
                        lookFor.results?.let {
                            insertAll(
                                productMapper.productResponseToProductEntity(lookFor.results)
                            )
                        }
                    } ?: Either.Left(
                        HandledError.Unknown(
                            UNKNOWN_MESSAGE_SERVER,
                            code = UNKNOWN
                        )
                    )
                }
            )
        }
    }

    override suspend fun insertAll(products: List<ProductEntity>): Either<HandledError, Boolean> {
        return withContext(dispatcher) {
            try {
                productSource.delete()
                productSource.insert(products)
                Either.Right(true)
            } catch (ex: java.lang.Exception) {
                Either.Left(
                    HandledError.UnExpected(
                        ex.message ?: UNEXPECTED_MESSAGE,
                        code = UNEXPECTED
                    )
                )
            }
        }
    }
}
