package com.wurple.data.gateway

import com.wurple.data.remote.account.AccountRemoteDataSource
import com.wurple.domain.gateway.AccountGateway

class DefaultAccountGateway(
    private val accountRemoteDataSource: AccountRemoteDataSource
) : AccountGateway {
    override suspend fun deleteAccount(currentUserId: String) {
        accountRemoteDataSource.deleteAccount(currentUserId)
    }
}