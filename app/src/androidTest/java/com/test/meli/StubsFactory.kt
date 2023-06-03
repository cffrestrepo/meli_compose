package com.test.meli

import com.test.meli.data.local.entities.ProductEntity
import com.test.meli.data.remote.response.Address
import com.test.meli.data.remote.response.LookFor
import com.test.meli.data.remote.response.Results
import com.test.meli.data.remote.response.Seller
import com.test.meli.repository.models.ResultsModel

const val PERRO = "Perro"
const val ID = "12345"
const val tHUMBNAIL = "https://bankimages.com/imagen.png"
const val PRICE = "5000"
const val NICKNAME = "Mercado libre"
const val ADDRESS = "cll 4 # 28 - 141"
const val CITY = "Zipaquira"

fun createLooFor() = LookFor(
    query = PERRO,
    results = listOf(createResults(), createResults())
)

fun createListResults() = listOf<Results>(createResults(), createResults())
fun createResults() = Results(
    id = ID,
    title = PERRO,
    thumbnail = tHUMBNAIL,
    price = PRICE,
    seller = createSeller(),
    address = createAddress()
)

fun createSeller() = Seller(
    nickname = NICKNAME
)

fun createAddress() = Address(
    address = ADDRESS,
    city_name = CITY
)

fun createListProductEntity() = listOf<ProductEntity>(createProductEntity(), createProductEntity())

fun createProductEntity() = ProductEntity(
    id = 0,
    title = PERRO,
    thumbnail = tHUMBNAIL,
    price = PRICE,
    nickname = NICKNAME,
    address = ADDRESS,
    city = CITY
)

fun createListResultsModel() = listOf<ResultsModel>(createResultsModel(), createResultsModel())

fun createResultsModel() = ResultsModel(
    title = PERRO,
    thumbnail = tHUMBNAIL,
    price = PRICE,
    nickname = NICKNAME,
    address = ADDRESS,
    city_name = CITY
)
