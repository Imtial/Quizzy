package com.example.quizzy.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


object ImageUtil {
    private const val REQ_WIDTH = 256
    private const val REQ_HEIGHT = 256
    fun getExtensionFromUri(context: Context, imageUri: Uri?): String? {
        return MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(imageUri?.let { context.contentResolver.getType(it) })
    }

    private fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight
                    && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    @Throws(IOException::class)
    fun convertToBytes(context: Context, uri: Uri?): ByteArray {
        val ext = getExtensionFromUri(context, uri)
        var input: InputStream? = uri?.let { context.contentResolver.openInputStream(it) }
        val byteOut = ByteArrayOutputStream()
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(input, null, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, REQ_WIDTH, REQ_HEIGHT)
        input?.close()
        input = uri?.let { context.contentResolver.openInputStream(it) }
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        val bitmap = BitmapFactory.decodeStream(input, null, options)
        if (bitmap != null) {
            if (ext.equals("jpg", ignoreCase = true)
                    || ext.equals("jpeg", ignoreCase = true)) bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteOut) else bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteOut)
        } else {
            Log.d("SYNCING", "convertToPngBytes: null")
        }
        val data: ByteArray = byteOut.toByteArray()
        input?.close()
        byteOut.flush()
        byteOut.close()
        return data
    }

    @Throws(IOException::class)
    fun convertToFile(context: Context, uri: Uri?, fileName: String): File {
        val ext = getExtensionFromUri(context, uri)
        var input: InputStream? = uri?.let { context.contentResolver.openInputStream(it) }
        val byteOut = ByteArrayOutputStream()
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(input, null, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, REQ_WIDTH, REQ_HEIGHT)
        input?.close()
        input = uri?.let { context.contentResolver.openInputStream(it) }
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        val bitmap = BitmapFactory.decodeStream(input, null, options)
        if (bitmap != null) {
            if (ext.equals("jpg", ignoreCase = true)
                    || ext.equals("jpeg", ignoreCase = true)) bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteOut) else bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteOut)
        } else {
            Log.d("SYNCING", "convertToPngBytes: null")
        }
        val data: ByteArray = byteOut.toByteArray()

        //create a file to write bitmap data
        Log.d("file name =>", "$fileName.$ext")
        val file = File(context.cacheDir, fileName)
        file.createNewFile()
        //        file.setWritable(true);
        Log.d("after create fileName=>", file.name)
        // Initialize a pointer
        // in file using OutputStream
        val fileOutputStream = FileOutputStream(file)
        // Starts writing the bytes in it
        Log.d("writing", "before writing to file")
        fileOutputStream.write(data)
        Log.d("writing", "after writing to file")
        fileOutputStream.flush()
        fileOutputStream.close()
        return file
    }

    fun toMultiPartFile(name: String?, imageFile: File): MultipartBody.Part {
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile)
        return MultipartBody.Part.createFormData(
                name,
                imageFile.name,  // filename, this is optional
                reqFile)
    }

    fun getBitmapFromURL(src: String): Bitmap? {
        return try {
            Log.e("src", src)
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            val myBitmap = BitmapFactory.decodeStream(input)
            Log.e("Bitmap", "returned")
            myBitmap
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Exception", e.message!!)
            null
        }
    }

    fun generateImageUrl(id: String): String {
        val baseUrl = "https://contest-quiz-app.herokuapp.com/users/"
        val queryKey = "/avatar"
        return baseUrl+id+queryKey
    }
}
