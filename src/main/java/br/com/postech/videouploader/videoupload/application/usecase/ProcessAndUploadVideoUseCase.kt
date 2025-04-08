package br.com.postech.videouploader.videoupload.application.usecase

import java.time.LocalDateTime
import java.util.*

interface ProcessAndUploadVideoUseCase {

    fun execute(
        userId: UUID,
        title: String,
        description: String,
        filePath: String,
        createdAt: LocalDateTime
    ): String
}