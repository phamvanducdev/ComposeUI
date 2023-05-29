package com.ducpv.composeui.domain.repository

import com.ducpv.composeui.domain.datastore.AuthDataStore
import com.ducpv.composeui.domain.model.UserInfo
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull

/**
 * Created by pvduc9773 on 24/05/2023.
 */
interface AuthRepository {
    suspend fun getUserInfo(): UserInfo?
    suspend fun setUserInfo(userInfo: UserInfo)
    suspend fun removeUserInfo()
}

class AuthRepositoryImpl @Inject constructor(
    private val authDataStore: AuthDataStore
) : AuthRepository {
    override suspend fun getUserInfo(): UserInfo? {
        return authDataStore.userInfo.firstOrNull()
    }

    override suspend fun setUserInfo(userInfo: UserInfo) {
        authDataStore.setUserInfo(userInfo)
    }

    override suspend fun removeUserInfo() {
        authDataStore.removeUserInfo()
    }
}
