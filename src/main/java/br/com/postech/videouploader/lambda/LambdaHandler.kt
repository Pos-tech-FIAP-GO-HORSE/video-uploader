package br.com.postech.videouploader.lambda

import br.com.postech.videouploader.application.usecase.ProcessAndUploadVideoUseCase
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.time.LocalDateTime
import java.util.*


class LambdaHandler : RequestHandler<Map<String, Any>, String> {

    private val applicationContext = AnnotationConfigApplicationContext("com.example.videoprocessing")
    private val processAndUploadVideoUseCase: ProcessAndUploadVideoUseCase =
        applicationContext.getBean(ProcessAndUploadVideoUseCase::class.java)

    override fun handleRequest(input: Map<String, Any>, context: Context): String {
        // Extrair os parâmetros do evento recebido
        val userId = input["userId"] as? String ?: throw IllegalArgumentException("userId is required")
        val title = input["title"] as? String ?: throw IllegalArgumentException("title is required")
        val description = input["description"] as? String ?: throw IllegalArgumentException("description is required")
        val filePath = input["filePath"] as? String ?: throw IllegalArgumentException("filePath is required")
        val createdAt = LocalDateTime.now()

        // Executar o caso de uso
        val s3Url = processAndUploadVideoUseCase.execute(
            userId = UUID.fromString(userId),
            title = title,
            description = description,
            filePath = filePath,
            createdAt = createdAt
        )

        // Retornar a URL do vídeo no S3
        return "Video uploaded successfully: $s3Url"
    }
}
