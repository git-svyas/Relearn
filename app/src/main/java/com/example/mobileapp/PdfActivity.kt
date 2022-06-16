package com.example.mobileapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnRenderListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class PdfActivity : AppCompatActivity() {

    lateinit var pdfViewer: PDFView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        pdfViewer = findViewById(R.id.pdf_viewer)
        val url: String? = intent.getStringExtra("EXTRA_URL")
        if(url == null){
            Log.d("Main", "url from the intent is null")
        }
        else{
            loadPdf(url)
        }

    }

    private fun loadPdf(link: String){
        GlobalScope.launch {
            var inputStream: InputStream? = null
            var url: URL = URL(link)

            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            if(connection.responseCode == 200){
                inputStream = BufferedInputStream(connection.inputStream)
            }
            Log.d("Main","Input Steam is ${inputStream}")
            pdfViewer.fromStream(inputStream)
                //.defaultPage(1)
                .onLoad(OnLoadCompleteListener {
                    Log.d("Main","Page: ${it} is loaded")
                })
                .onRender {
                    Log.d("Main","Document Rendedred ${it}")
                }
                //.nightMode(true)
                .load()
        }
    }
}