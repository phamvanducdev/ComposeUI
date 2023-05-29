package com.ducpv.composeui.domain.usecase

import com.ducpv.composeui.core.util.AppDispatcher
import com.ducpv.composeui.domain.model.UserInfo
import com.ducpv.composeui.domain.repository.AuthRepository
import com.ducpv.composeui.domain.repository.FireStoreRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Created by pvduc9773 on 24/05/2023.
 */
class SignUpUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authRepository: AuthRepository,
    private val fireStoreRepository: FireStoreRepository,
    private val dispatcher: AppDispatcher,
) {
    suspend operator fun invoke(email: String, password: String) {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            ?: throw SignUpException.FailedToSignUp
        val firebaseUser = authResult.user ?: throw SignUpException.FailedToGetUserInfo
        val userInfo = UserInfo(
            uid = firebaseUser.uid,
            email = firebaseUser.email ?: email,
        )
        fireStoreRepository.setUserInfo(userInfo)
        withContext(dispatcher.io) {
            authRepository.setUserInfo(userInfo)
        }
    }

    sealed class SignUpException : Exception() {
        object FailedToSignUp : SignUpException()
        object FailedToGetUserInfo : SignUpException()
    }
}
