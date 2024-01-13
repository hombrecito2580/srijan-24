package com.iitism.srijan24.utils

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import java.util.*

class CloudinaryUploader(context: Context) {

    init {
        // Initialize Cloudinary with your credentials
//        MediaManager.init(context)
//            .cloudName("digvpmszg")
//            .apiKey("346224682169534")
//            .apiSecret("c7Eip5uGeMBUYxU8ta4iGn51qPo")
    }

    fun uploadImage(uri: Uri, callback: UploadCallback) {

        if (uri != null) {
            // Generate a unique public ID for the uploaded image
            val publicId = "android_upload_${UUID.randomUUID()}"

            // Perform the image upload
            MediaManager.get().upload(uri)
                .option("public_id", publicId)
                .callback(callback)
                .dispatch()
        } else {
            // Handle the case where opening the InputStream failed
        }
    }
}
