package br.com.postech.videoupload.infra.s3

import org.springframework.stereotype.Component
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.File

@Component
class S3Uploader(
    private val s3Client: S3Client,
    private val s3Properties: S3Properties
) {

    fun upload(filePath: String, key: String): String {
        val file = File(filePath)
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(s3Properties.bucketName)
            .key(key)
            .build()

        s3Client.putObject(putObjectRequest, file.toPath())
        return "https://${s3Properties.bucketName}.s3.amazonaws.com/$key"
    }
}