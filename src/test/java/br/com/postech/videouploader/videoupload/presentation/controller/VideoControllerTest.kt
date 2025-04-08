package br.com.postech.videouploader.videoupload.presentation.controller

import br.com.postech.videouploader.videoupload.application.usecase.ProcessAndUploadVideoUseCase
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime
import java.util.*

@WebMvcTest(VideoController::class)
class VideoControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {
    private val processAndUploadVideoUseCase: ProcessAndUploadVideoUseCase = mock(ProcessAndUploadVideoUseCase::class.java)

    @Test
    fun `should upload video and return S3 URL`() {
        // Mock do caso de uso
        val videoId = UUID.randomUUID()
        val s3Url = "https://bucket-name.s3.amazonaws.com/videos/$videoId.mp4"
        `when`(processAndUploadVideoUseCase.execute(eq(videoId), anyString(), anyString(), anyString(), LocalDateTime.now())).thenReturn(s3Url)

        // Mock do arquivo de upload
        val mockFile = MockMultipartFile(
            "file", "video.mp4", MediaType.MULTIPART_FORM_DATA_VALUE, "video content".toByteArray()
        )

        // Teste do endpoint
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/videos/$videoId/upload")
                .file(mockFile)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(s3Url))
    }
}