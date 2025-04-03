package com.example.cafelabservice.service

import org.springframework.stereotype.Service
import java.io.File

@Service
class BlobService(
    private val vercelService: VercelService
) {
    suspend fun uploadPdfToBlob(fileName: String, pdfData: ByteArray): String {
        val file = File(fileName)
        file.writeBytes(pdfData)
        return vercelService.uploadFileToVercelBlob(file) ?: throw Exception("Failed to upload $fileName to Vercel Blob")
    }
}