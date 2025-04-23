package br.com.postech.videoupload.lambda

import br.com.postech.videoupload.VideoUploadApplication
import br.com.postech.videoupload.application.usecase.ProcessAndUploadVideoUseCase
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.PublishRequest
import java.nio.file.Files
import java.time.LocalDateTime
import java.util.*

@Profile("lambda")
@SpringBootApplication
@ComponentScan(basePackages = ["br.com.postech.videoupload"])
@EnableAspectJAutoProxy(proxyTargetClass = false)
class LambdaHandler : RequestHandler<Map<String, Any>, String> {

    private val context: ApplicationContext by lazy {
        SpringApplication.run(VideoUploadApplication::class.java)
    }

    private val useCase: ProcessAndUploadVideoUseCase by lazy {
        context.getBean(ProcessAndUploadVideoUseCase::class.java)
    }

    override fun handleRequest(input: Map<String, Any>, context: Context): String {
        val logger = LoggerFactory.getLogger(LambdaHandler::class.java)

        val headers = input["headers"] as? Map<*, *> ?: throw IllegalArgumentException("Missing headers")
        val authHeader = headers["Authorization"] as? String ?: throw IllegalArgumentException("Missing Authorization header")

        val token = authHeader.removePrefix("Bearer ").trim()
        val userId = extractUserIdFromToken(token)

        val title = input.requireString("title")
        val description = input.requireString("description")
        val fileBase64 = input.requireString("fileBase64")

        val bytes = try {
            Base64.getDecoder().decode(fileBase64)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("fileBase64 is not valid Base64")
        }

        val tempFile = Files.createTempFile("video_", ".mp4").toFile()

        return try {
            tempFile.writeBytes(bytes)

            val s3Url = useCase.execute(
                userId = UUID.fromString(userId),
                title = title,
                description = description,
                filePath = tempFile.absolutePath,
                createdAt = LocalDateTime.now()
            )

            val videoKey = s3Url.substringAfterLast("/")

            val messageJson = """
                {
                    "video_key": "$videoKey",
                    "s3Url": "$s3Url",
                    "user_email": "postechfiap7@gmail.com",
                    "user_id": "$userId"
                }
            """.trimIndent()

            val publishRequest = PublishRequest.builder()
                .topicArn(snsTopicArn)
                .message(messageJson)
                .build()

            snsClient.publish(publishRequest)

            logger.info("Video uploaded and message published for userId=$userId, videoKey=$videoKey")
            "Video uploaded successfully: $s3Url"
        } finally {
            tempFile.delete()
        }
    }

    private fun extractUserIdFromToken(token: String): String {
        val claims: Claims = Jwts.parserBuilder()
            .setSigningKey(System.getenv("JWT_SECRET_KEY").toByteArray())
            .build()
            .parseClaimsJws(token)
            .body

        return claims.subject ?: throw IllegalArgumentException("Token inválido: subject ausente")
    }

    companion object {
        private val snsClient: SnsClient = SnsClient.builder().build()
        private const val snsTopicArn = "arn:aws:sns:us-east-1:852121054528:video-process-trigger"
    }
}

// Extension function para simplificar extração de dados obrigatórios
private fun Map<String, Any>.requireString(key: String): String =
    this[key] as? String ?: throw IllegalArgumentException("$key is required and must be a string")
