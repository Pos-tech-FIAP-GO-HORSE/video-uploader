package br.com.postech.videoupload.application.service

import br.com.postech.videoupload.domain.model.Video
import br.com.postech.videoupload.domain.repository.VideoRepository
import br.com.postech.videoupload.infra.s3.S3Uploader
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.*

class ProcessAndUploadVideoServiceTest {

    private val videoRepository = mock<VideoRepository>()
    private val s3Uploader = mock<S3Uploader>()
    private val service = ProcessAndUploadVideoService(videoRepository, s3Uploader)

    @Test
    fun `should upload video and return S3 url`() {
        val userId = UUID.randomUUID()
        val title = "Test Title"
        val description = "Test Description"
        val filePath = "/tmp/test.mp4"
        val createdAt = LocalDateTime.now()
        val s3Url = "https://s3.amazonaws.com/bucket/video.mp4"

        // Stub save() to return the video (with or without URL, doesn't matter)
        whenever(videoRepository.save(any())).thenAnswer { it.arguments[0] as Video }
        whenever(s3Uploader.upload(filePath, any())).thenReturn(s3Url)

        val result = service.execute(userId, title, description, filePath, createdAt)

        assertEquals(s3Url, result)
        verify(videoRepository, times(2)).save(any()) // one before upload, one after
        verify(s3Uploader).upload(eq(filePath), any())
    }
}