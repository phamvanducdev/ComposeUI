package com.ducpv.composeui.domain.usecase.auth

import com.ducpv.composeui.core.util.AppDispatcher
import com.ducpv.composeui.domain.datastore.AuthDataStore
import com.ducpv.composeui.domain.datastore.toDataStoreUser
import com.ducpv.composeui.domain.firestore.model.FireStoreUser
import com.ducpv.composeui.domain.firestore.model.toUser
import com.ducpv.composeui.domain.repository.FireStoreRepository
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Created by pvduc9773 on 24/05/2023.
 */
class SignUpUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authDataStore: AuthDataStore,
    private val fireStoreRepository: FireStoreRepository,
    private val dispatcher: AppDispatcher,
) {
    suspend operator fun invoke(email: String, password: String) {
        val authResult =
            firebaseAuth.createUserWithEmailAndPassword(email, password).await() ?: throw SignUpException.FailedToSignUp
        val firebaseUser = authResult.user ?: throw SignUpException.FailedToGetUserInfo
        fireStoreRepository.insertUser(
            FireStoreUser(
                uid = firebaseUser.uid,
                email = firebaseUser.email ?: email,
                name = null,
                avatar = null,
                createdAt = Date(),
                updatedAt = Date(),
            ),
        )
        val user = fireStoreRepository.getUser(firebaseUser.uid)?.toUser() ?: throw SignUpException.FailedToGetUserInfo
        withContext(dispatcher.io) {
            authDataStore.setDataStoreUser(user.toDataStoreUser())
        }
    }

    sealed class SignUpException : Exception() {
        object FailedToSignUp : SignUpException()
        object FailedToGetUserInfo : SignUpException()
    }
}
