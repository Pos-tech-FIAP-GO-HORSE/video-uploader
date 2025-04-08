package br.com.postech.videouploader.videoupload.infra.s3

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.File

@Component
class S3Uploader(
    private val s3Client: S3Client,

    @Value("\${aws.s3.bucket-name}")
    private val bucketName: String
) {

    fun upload(filePath: String, key: String): String {
        val file = File(filePath)
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        s3Client.putObject(putObjectRequest, file.toPath())
        return "https://$bucketName.s3.amazonaws.com/$key"
    }
}