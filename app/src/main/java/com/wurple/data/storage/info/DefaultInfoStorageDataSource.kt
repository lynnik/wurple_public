package com.wurple.data.storage.info

import com.wurple.data.datastore.InfoDataStore

class DefaultInfoStorageDataSource(
    private val infoDataStore: InfoDataStore
) : InfoStorageDataSource {
    override suspend fun shouldShowFillOutProfile(): Boolean {
        return infoDataStore.shouldShowFillOutProfile()
    }

    override suspend fun setFillOutProfileAsShown() {
        infoDataStore.setFillOutProfileAsShown()
    }

    override suspend fun clearCache() {
        infoDataStore.clearDataStore()
    }
}
