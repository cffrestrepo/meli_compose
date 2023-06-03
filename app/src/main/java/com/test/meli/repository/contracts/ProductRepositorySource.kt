package com.test.meli.repository.contracts

import com.test.meli.commons.Either
import com.test.meli.data.local.entities.ProductEntity
import com.test.meli.data.network.HandledError
import com.test.meli.repository.models.ResultsModel

interface ProductRepositorySource {

    suspend fun getAll(): Either<HandledError, List<ResultsModel>>
    suspend fun fetchRemote(query: String): Either<HandledError, Boolean>
    suspend fun insertAll(product: List<ProductEntity>): Either<HandledError, Boolean>
}