package br.com.postech.videoupload.lambda

import br.com.postech.videoupload.application.usecase.ProcessAndUploadVideoUseCase
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.EnableAspectJAutoProxy
import java.nio.file.Files
import java.time.LocalDateTime
import java.util.*

@SpringBootApplication
@ComponentScan(basePackages = ["br.com.postech.videoupload"])
@EnableAspectJAutoProxy(proxyTargetClass = false) // Se você estiver usando AOP
class LambdaHandler : RequestHandler<Map<String, Any>, String> {

    private val applicationContext: ApplicationContext = SpringApplicationBuilder(LambdaHandler::class.java).run()

    private val processAndUploadVideoUseCase: ProcessAndUploadVideoUseCase =
        applicationContext.getBean(ProcessAndUploadVideoUseCase::class.java)

    override fun handleRequest(input: Map<String, Any>, context: Context): String {
        val userId = input["userId"] as? String ?: throw IllegalArgumentException("userId is required")
        val title = input["title"] as? String ?: throw IllegalArgumentException("title is required")
        val description = input["description"] as? String ?: throw IllegalArgumentException("description is required")
        val fileBase64 = input["fileBase64"] as? String ?: throw IllegalArgumentException("fileBase64 is required")

        // Decode base64 e salva em arquivo temporário
        val bytes = Base64.getDecoder().decode(fileBase64)
        val tempFile = Files.createTempFile("video_", ".mp4").toFile()
        tempFile.writeBytes(bytes)

        val createdAt = LocalDateTime.now()

        val s3Url = processAndUploadVideoUseCase.execute(
            userId = UUID.fromString(userId),
            title = title,
            description = description,
            filePath = tempFile.absolutePath,
            createdAt = createdAt
        )

        return "Video uploaded successfully: $s3Url"
    }
}