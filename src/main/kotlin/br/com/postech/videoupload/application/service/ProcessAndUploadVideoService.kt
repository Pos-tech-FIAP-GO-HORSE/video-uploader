package br.com.postech.videoupload.application.service

import br.com.postech.videoupload.application.usecase.ProcessAndUploadVideoUseCase
import br.com.postech.videoupload.domain.model.Video
import br.com.postech.videoupload.domain.repository.VideoRepository
import br.com.postech.videoupload.infra.s3.S3Uploader
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ProcessAndUploadVideoService(
    private val videoRepository: VideoRepository,
    private val s3Uploader: S3Uploader
) : ProcessAndUploadVideoUseCase {

    override fun execute(
        userId: UUID,
        title: String,
        description: String,
        filePath: String,
        createdAt: LocalDateTime
    ): String {
        // 1. Criar o vídeo e salvar no repositório
        val video = Video(
            id = UUID.randomUUID(),
            userId = userId,
            title = title,
            description = description,
            createdAt = createdAt,
            url = null // URL será preenchida após o upload
        )
        videoRepository.save(video)

        // 2. Fazer o upload do arquivo para o S3
        val s3Url = s3Uploader.upload(filePath, "videos/${video.id}.mp4")

        // 3. Atualizar o vídeo com a URL do S3
        val updatedVideo = video.copy(url = s3Url)
        videoRepository.save(updatedVideo)

        // 4. Retornar a URL do S3
        return s3Url
    }
}