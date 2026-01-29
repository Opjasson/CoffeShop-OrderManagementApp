package com.example.cafecornerapp.Helper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.view.View
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class HandlePrint {
    fun View.toBitmap(): Bitmap {
        measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        layout(0, 0, measuredWidth, measuredHeight)

        val bitmap = Bitmap.createBitmap(
            measuredWidth,
            measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

    fun createPdfFromBitmap(
        context: Context,
        bitmap: Bitmap,
        fileName: String
    ): File {
        val pdfDocument = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(
            bitmap.width,
            bitmap.height,
            1
        ).create()

        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)

        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "$fileName.pdf"
        )

        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        return file
    }


}