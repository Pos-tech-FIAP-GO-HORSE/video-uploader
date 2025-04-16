package br.com.postech.videoupload.infra.s3

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "aws.s3")
class S3Properties {
    lateinit var bucketName: String
    lateinit var region: String
    lateinit var accessKey: String
    lateinit var secretKey: String
}