package com.test.meli.usecases

import com.test.meli.PERRO
import com.test.meli.commons.Constants
import com.test.meli.commons.Either
import com.test.meli.createListResultsModel
import com.test.meli.data.network.HandledError
import com.test.meli.repository.contracts.ProductRepositorySource
import com.test.meli.repository.models.ResultsModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/***
 * Test class of [GetAllProductsUseCase]
 */
@ExperimentalCoroutinesApi
class GetAllProductsUseCaseTest {

    @MockK
    lateinit var productRepository: ProductRepositorySource

    lateinit var getAllProductsUseCase: GetAllProductsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(
            this,
            relaxUnitFun = true
        )
        getAllProductsUseCase = GetAllProductsUseCase(productRepository)
    }

    @After
    fun tearDown() = clearAllMocks()

    @Test
    fun `given-a-call-to-the-execute-method-when-successful-returns-listresultmodel`() =
        runBlocking {
            // GIVEN
            val listResultsModel = createListResultsModel()
            coEvery { productRepository.getAll() } returns Either.Right(listResultsModel)

            // WHEN
            val result = getAllProductsUseCase.execute(Unit)

            // VERIFY
            assert(result is Either.Right)
            Assert.assertEquals((result as Either.Right).value, listResultsModel)
            coVerifyOrder {
                productRepository.getAll()
            }
        }

    @Test
    fun `given-a-call-to-the-execute-method-when-failed-returns-handlederror`() =
        runBlocking {
            // GIVEN
            val handledError = HandledError.UnExpected(
                Constants.UNEXPECTED_MESSAGE,
                code = Constants.UNEXPECTED
            )
            coEvery { productRepository.getAll() } returns Either.Left(handledError)

            // WHEN
            val result = getAllProductsUseCase.execute(Unit)

            // VERIFY
            assert(result is Either.Left)
            Assert.assertEquals((result as Either.Left).error, handledError)
            coVerifyOrder {
                productRepository.getAll()
            }
        }
}
