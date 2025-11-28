package team.iscode.igor.calculosinais

import android.content.ContentValues
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import team.iscode.igor.calculosinais.databinding.ActivityCalibrationValuesBinding
import java.io.File
import java.io.FileOutputStream

class PDFActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalibrationValuesBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 🔥 Fixar densidade do layout para PDF (dá tamanho correto A4)
        val config = resources.configuration
        config.densityDpi = 150   // densidade real de PDFs (A4)
        val fixedContext = createConfigurationContext(config)
        val inflater = LayoutInflater.from(fixedContext)

        // Recarregar o teu layout XML usando a densidade fixa
        val view = inflater.inflate(R.layout.layout_pdf, null)

        // Tamanho A4 a 72dpi
        val width = 595
        val height = 842

        // Medir o layout
        view.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        )

        view.layout(0, 0, width, height)


        // Criar PDF
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()
        val page = document.startPage(pageInfo)

        view.draw(page.canvas)
        document.finishPage(page)

        // Guardar PDF
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, "calibracao.pdf")
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
        }

        val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            contentResolver.openOutputStream(uri)?.use { out ->
                document.writeTo(out)
            }
        }

        // 5 — Fechar documento
        document.close()

        /* abre o ficheiro com app PDF predefinida no telemovel
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY

        startActivity(intent)
        */




        // Fechar esta activity depois de abrir o PDF
        finish()
    }
}