package com.ducpv.composeui.shared.extension

import com.ducpv.composeui.BuildConfig
import com.google.firebase.storage.FirebaseStorage

/**
 * Created by pvduc9773 on 11/06/2023.
 */
val firebaseStorage: FirebaseStorage get() = FirebaseStorage.getInstance(BuildConfig.STORAGE_BUCKET)

fun String.toFirebaseStorage() = firebaseStorage.reference.child(this)
