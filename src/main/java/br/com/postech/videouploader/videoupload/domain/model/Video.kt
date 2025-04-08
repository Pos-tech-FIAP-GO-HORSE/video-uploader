package br.com.postech.videouploader.videoupload.domain.model

import java.time.LocalDateTime
import java.util.*

data class Video(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    val url: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val userId: UUID
)
