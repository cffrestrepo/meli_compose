package com.test.meli.usecases

import com.test.meli.PERRO
import com.test.meli.commons.Constants
import com.test.meli.commons.Either
import com.test.meli.data.network.HandledError
import com.test.meli.repository.contracts.ProductRepositorySource
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

/***
 * Test class of [FetchRemoteProductsUseCase]
 */
@ExperimentalCoroutinesApi
class FetchRemoteProductsUseCaseTest {

    @MockK
    lateinit var productRepository: ProductRepositorySource

    lateinit var fetchRemoteProductsUseCase: FetchRemoteProductsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(
            this,
            relaxUnitFun = true
        )
        fetchRemoteProductsUseCase = FetchRemoteProductsUseCase(productRepository)
    }

    @After
    fun tearDown() = clearAllMocks()

    @Test
    fun `given-a-call-to-the-execute-method-when-successful-returns-true`() =
        runBlocking {
            // GIVEN
            val params = PERRO
            coEvery { productRepository.fetchRemote(any()) } returns Either.Right(true)

            // WHEN
            val result = fetchRemoteProductsUseCase.execute(params)

            // VERIFY
            assert(result is Either.Right)
            coVerifyOrder {
                productRepository.fetchRemote(params)
            }
        }

    @Test
    fun `given-a-call-to-the-execute-method-when-failed-returns-handlederror`() =
        runBlocking {
            // GIVEN
            val params = PERRO
            val handledError = HandledError.UnExpected(
                Constants.UNEXPECTED_MESSAGE,
                code = Constants.UNEXPECTED
            )

            coEvery { productRepository.fetchRemote(any()) } returns Either.Left(handledError)

            // WHEN
            val result = fetchRemoteProductsUseCase.execute(params)

            // VERIFY
            assert(result is Either.Left)
            coVerifyOrder {
                productRepository.fetchRemote(params)
            }
        }
}
