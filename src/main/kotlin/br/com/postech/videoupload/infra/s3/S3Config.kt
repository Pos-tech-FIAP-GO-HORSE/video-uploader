package br.com.postech.videoupload.infra.s3

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class S3Config {

    @Bean
    fun s3Client(): S3Client {

        val region = System.getenv("AWS_REGION") ?: "sa-east-1"


        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build()
    }
}

/*@Configuration
open class S3Config(
    private val s3Properties: S3Properties
) {

    @Bean
    open fun s3Client(): S3Client {
        val credentials = AwsBasicCredentials.create(
            s3Properties.accessKey,
            s3Properties.secretKey
        )

        return S3Client.builder()
            .region(Region.of(s3Properties.region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()
    }
}*/