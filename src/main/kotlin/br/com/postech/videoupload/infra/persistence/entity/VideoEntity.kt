package br.com.postech.videoupload.infra.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "videos")
data class VideoEntity(
    @Id
    val id: UUID = UUID.randomUUID(),

    val title: String,

    val description: String,

    val url: String? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "user_id")
    val userId: UUID
)
