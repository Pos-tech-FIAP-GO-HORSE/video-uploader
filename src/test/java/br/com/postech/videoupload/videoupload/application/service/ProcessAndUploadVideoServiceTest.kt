package br.com.postech.videoupload.videoupload.application.service

import br.com.postech.videoupload.application.service.ProcessAndUploadVideoService
import br.com.postech.videoupload.domain.model.Video
import br.com.postech.videoupload.domain.repository.VideoRepository
import br.com.postech.videoupload.infra.s3.S3Uploader
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.util.*

class ProcessAndUploadVideoServiceTest {

    private val videoRepository: VideoRepository = mock(VideoRepository::class.java)
    private val s3Uploader: S3Uploader = mock(S3Uploader::class.java)
    private val processAndUploadVideoService = ProcessAndUploadVideoService(videoRepository, s3Uploader)

    @Test
    fun `should process and upload video`() {
        // Mock do vídeo
        val videoId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val title = "Sample Video"
        val description = "This is a sample video description"
        val createdAt = LocalDateTime.now()
        val video =  Video(
            id = videoId,
            title = title,
            description = description,
            url = null,
            createdAt = createdAt,
            userId = userId
        )
        `when`(videoRepository.findById(videoId)).thenReturn(video)

        // Mock do upload para o S3
        val filePath = "/path/to/video.mp4"
        val s3Url = "https://bucket-name.s3.amazonaws.com/videos/$videoId.mp4"
        `when`(s3Uploader.upload(filePath, "videos/$videoId.mp4")).thenReturn(s3Url)

        // Executar o caso de uso
        val result = processAndUploadVideoService.execute(videoId, anyString(), anyString(), anyString(), LocalDateTime.now())

        // Verificar resultados
        assertEquals(s3Url, result)
        verify(videoRepository).save(video.copy(url = s3Url))
    }
}