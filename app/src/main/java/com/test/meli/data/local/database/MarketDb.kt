package com.test.meli.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.meli.commons.Constants.Companion.VERSION_DATA_BASE
import com.test.meli.data.local.dao.ProductDao
import com.test.meli.data.local.entities.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = VERSION_DATA_BASE
)
abstract class MarketDb : RoomDatabase() {

    abstract fun productDao(): ProductDao
}