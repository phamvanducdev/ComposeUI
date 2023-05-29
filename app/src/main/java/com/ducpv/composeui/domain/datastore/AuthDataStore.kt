package com.ducpv.composeui.domain.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ducpv.composeui.domain.model.UserInfo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Created by pvduc9773 on 24/05/2023.
 */
interface AuthDataStore {
    val userInfo: Flow<UserInfo?>

    suspend fun setUserInfo(userInfo: UserInfo)

    suspend fun removeUserInfo()
}

class AuthDataStoreImpl @Inject constructor(
    dataStore: DataStore<Preferences>
) : BaseDataStore(dataStore), AuthDataStore {
    companion object {
        private val USER_INFO_KEY = stringPreferencesKey("user-info")
    }

    override val userInfo: Flow<UserInfo?> = objectFlowOf(USER_INFO_KEY, null)

    override suspend fun setUserInfo(userInfo: UserInfo) {
        setObject(USER_INFO_KEY, userInfo)
    }

    override suspend fun removeUserInfo() {
        dataStore.edit {
            it.clear()
        }
    }
}
