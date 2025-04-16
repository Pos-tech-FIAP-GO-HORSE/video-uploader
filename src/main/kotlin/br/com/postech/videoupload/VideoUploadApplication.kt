package br.com.postech.videoupload

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication

class VideoUploadApplication

fun main(args: Array<String>) {
    runApplication<VideoUploadApplication>(*args)
}