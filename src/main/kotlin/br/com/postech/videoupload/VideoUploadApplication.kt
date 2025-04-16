package br.com.postech.videoupload

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories("br.com.postech.videoupload.infra.persistence")
@EntityScan("br.com.postech.videoupload.infra.persistence.entity")
open class VideoUploadApplication

fun main(args: Array<String>) {
    runApplication<VideoUploadApplication>(*args)
}