package com.wurple.presentation.manager

import android.content.Context
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.wurple.R
import com.wurple.domain.dispatcher.DispatcherProvider
import com.wurple.domain.log.Logger
import com.wurple.presentation.extension.getColorCompat
import kotlinx.coroutines.withContext

class QrManager(
    private val context: Context,
    private val dispatcherProvider: DispatcherProvider,
    private val logger: Logger
) {
    suspend fun generateQrCode(text: String): Bitmap? {
        return withContext(dispatcherProvider.default) {
            val screenWidth = context.resources.displayMetrics.widthPixels
            val width = screenWidth / 2
            val height = screenWidth / 2
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val codeWriter = MultiFormatWriter()
            try {
                val bitMatrix = codeWriter.encode(
                    text,
                    BarcodeFormat.QR_CODE,
                    width,
                    height,
                    mapOf(EncodeHintType.MARGIN to 0)
                )
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        val color = context.getColorCompat(
                            if (bitMatrix[x, y]) {
                                R.color.md_theme_onBackground
                            } else {
                                R.color.qrCode
                            }
                        )
                        bitmap.setPixel(x, y, color)
                    }
                }
                bitmap
            } catch (throwable: Throwable) {
                logger.e(TAG, throwable)
                null
            }
        }
    }

    companion object {
        private const val TAG = "QrManager"
    }
}