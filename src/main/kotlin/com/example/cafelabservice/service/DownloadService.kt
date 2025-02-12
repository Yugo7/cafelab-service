package com.example.cafelabservice.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class DownloadService(
    private val blobService: BlobService
) {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
    }

    suspend fun downloadPdf(url: String): ByteArray {
        println("Downloading PDF from: $url")
        return withContext(Dispatchers.IO) {
            val response: HttpResponse = client.get(url)
            if (!response.status.isSuccess()) {
                throw IOException("Unexpected response code: ${response.status}")
            }
            response.body()
        }
    }

    suspend fun downloadAndUploadPdf(pdfUrl: String, orderId: String): String {
        val pdfData = downloadPdf(pdfUrl)
        return blobService.uploadPdfToBlob("receipt-order$orderId${System.currentTimeMillis()}", pdfData)
    }
}