package com.example.moviedb.network

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class SaveImageWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    companion object {
        const val IMAGE_URL_KEY = "image_url_key"
    }

    override fun doWork(): Result {
        val imageUrl = inputData.getString("image_url_key") ?: return Result.failure()

        return try {
            val bitmap = downloadImage(imageUrl)
            saveToGallery(bitmap)
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun downloadImage(url: String): Bitmap {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val input = connection.inputStream
        return BitmapFactory.decodeStream(input)
    }

    private fun saveToGallery(bitmap: Bitmap) {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = applicationContext.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            imageUri?.let {
                resolver.openOutputStream(it).use { out ->
                    if (out != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    }
                }
            }
        } else {
            val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val file = File(directory, filename)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }

            MediaScannerConnection.scanFile(applicationContext, arrayOf(file.toString()), null, null)
        }
    }
}