package com.ducpv.composeui.domain.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ducpv.composeui.domain.model.chat.User
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Created by pvduc9773 on 24/05/2023.
 */
interface AuthDataStore {
    val subscribeUser: Flow<DataStoreUser?>
    suspend fun getDataStoreUser(): DataStoreUser?
    suspend fun setDataStoreUser(user: DataStoreUser)
    suspend fun clear()

    suspend fun getUserInfo(): User? {
        return getDataStoreUser()?.toUser()
    }
}

class AuthDataStoreImpl @Inject constructor(
    dataStore: DataStore<Preferences>
) : BaseDataStore(dataStore), AuthDataStore {
    companion object {
        private val USER_KEY = stringPreferencesKey("user")
    }

    override val subscribeUser: Flow<DataStoreUser?> = objectFlowOf<DataStoreUser>(USER_KEY, null)

    override suspend fun getDataStoreUser(): DataStoreUser? {
        return getObject<DataStoreUser>(USER_KEY)
    }

    override suspend fun setDataStoreUser(user: DataStoreUser) {
        setObject(USER_KEY, user)
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
