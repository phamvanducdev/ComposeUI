package com.ducpv.composeui.domain.usecase.auth

import com.ducpv.composeui.core.util.AppDispatcher
import com.ducpv.composeui.domain.datastore.AuthDataStore
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlinx.coroutines.withContext

/**
 * Created by pvduc9773 on 24/05/2023.
 */
class SignOutUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authDataStore: AuthDataStore,
    private val dispatcher: AppDispatcher,
) {
    suspend operator fun invoke() {
        firebaseAuth.signOut()
        withContext(dispatcher.io) {
            authDataStore.clear()
        }
    }
}
