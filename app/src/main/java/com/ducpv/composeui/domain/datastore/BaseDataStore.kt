package com.ducpv.composeui.domain.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

/**
 * Created by pvduc9773 on 24/05/2023.
 */
abstract class BaseDataStore(val dataStore: DataStore<Preferences>) {

    @OptIn(ExperimentalSerializationApi::class)
    val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    val data = dataStore.data

    suspend fun <T> get(key: Preferences.Key<T>): T? {
        return data.map { it[key] }.firstOrNull()
    }

    suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }

    suspend inline fun <reified T> setObject(key: Preferences.Key<String>, value: T) {
        try {
            set(key, json.encodeToString(value))
        } catch (e: SerializationException) {
            Timber.e("Failed to encode to string", e)
        }
    }

    suspend inline fun <reified T> getObject(key: Preferences.Key<String>): T? {
        val string = get(key) ?: return null
        return json.decodeFromString(string)
    }

    inline fun <reified T> objectFlowOf(key: Preferences.Key<String>, initialValue: T?): Flow<T?> {
        return data.map {
            val jsonString = it[key] ?: return@map initialValue
            json.decodeFromString(jsonString)
        }
    }
}

