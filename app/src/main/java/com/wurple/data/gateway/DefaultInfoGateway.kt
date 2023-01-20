package com.wurple.data.gateway

import com.wurple.data.storage.info.InfoStorageDataSource
import com.wurple.domain.gateway.InfoGateway

class DefaultInfoGateway(
    private val infoStorageDataSource: InfoStorageDataSource
) : InfoGateway {
    override suspend fun shouldShowFillOutProfile(): Boolean {
        return infoStorageDataSource.shouldShowFillOutProfile()
    }

    override suspend fun setFillOutProfileAsShown() {
        infoStorageDataSource.setFillOutProfileAsShown()
    }

    override suspend fun clearCache() {
        infoStorageDataSource.clearCache()
    }
}
