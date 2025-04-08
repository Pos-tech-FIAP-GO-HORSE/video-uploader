package br.com.postech.videouploader.videoupload.presentation.controller

import br.com.postech.videouploader.videoupload.application.usecase.ProcessAndUploadVideoUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/api/videos")
class VideoController(
    private val processAndUploadVideoUseCase: ProcessAndUploadVideoUseCase
) {

    @PostMapping("/{videoId}/upload")
    fun uploadVideo(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("title") title: String,
        @RequestParam("description") description: String,
        @RequestParam("userId") userId: UUID
    ): ResponseEntity<String> {
        // Salvar o arquivo localmente (ou em um diretório temporário)
        val tempFile = kotlin.io.path.createTempFile(suffix = file.originalFilename)
        file.transferTo(tempFile.toFile())

        // Processar e fazer o upload do vídeo
        val createdAt = LocalDateTime.now()
        val s3Url = processAndUploadVideoUseCase
            .execute(
                userId = userId,
                title = title,
                description = description,
                filePath = tempFile.toString(),
                createdAt = createdAt
            )

        // Retornar a URL do vídeo no S3
        return ResponseEntity.ok(s3Url)
    }
}