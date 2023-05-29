package com.ducpv.composeui.domain.usecase

import com.ducpv.composeui.core.util.AppDispatcher
import com.ducpv.composeui.domain.repository.AuthRepository
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
    private val authRepository: AuthRepository,
    private val fireStoreRepository: FireStoreRepository,
    private val dispatcher: AppDispatcher,
) {
    suspend operator fun invoke(email: String, password: String) {
        val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            ?: throw SignInException.FailedToSignIn
        val uid = authResult.user?.uid ?: throw SignInException.FailedToGetUserInfo
        val userInfo = fireStoreRepository.getUserInfo(uid) ?: throw SignInException.FailedToGetUserInfo
        withContext(dispatcher.io) {
            authRepository.setUserInfo(userInfo)
        }
    }

    sealed class SignInException : Exception() {
        object FailedToSignIn : SignInException()
        object FailedToGetUserInfo : SignInException()
    }
}
