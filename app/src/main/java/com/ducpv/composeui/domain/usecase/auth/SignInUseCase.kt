package com.ducpv.composeui.domain.usecase.auth

import com.ducpv.composeui.core.util.AppDispatcher
import com.ducpv.composeui.domain.datastore.AuthDataStore
import com.ducpv.composeui.domain.datastore.toDataStoreUser
import com.ducpv.composeui.domain.repository.FireStoreRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Created by pvduc9773 on 24/05/2023.
 */
class SignInUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authDataStore: AuthDataStore,
    private val fireStoreRepository: FireStoreRepository,
    private val dispatcher: AppDispatcher,
) {
    suspend operator fun invoke(email: String, password: String) {
        val authResult =
            firebaseAuth.signInWithEmailAndPassword(email, password).await() ?: throw SignInException.FailedToSignIn
        val uid = authResult.user?.uid ?: throw SignInException.FailedToGetUserInfo
        val user = fireStoreRepository.getUser(uid) ?: throw SignInException.FailedToGetUserInfo
        withContext(dispatcher.io) {
            authDataStore.setDataStoreUser(user.toDataStoreUser())
        }
    }

    sealed class SignInException : Exception() {
        object FailedToSignIn : SignInException()
        object FailedToGetUserInfo : SignInException()
    }
}
