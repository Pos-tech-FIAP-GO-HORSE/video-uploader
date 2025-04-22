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
    val id: UUID,
    val title: String,
    val description: String,
    val url: String,
    @Column(name = "user_id")
    val userId: UUID,
    @Column(name = "created_at")
    val createdAt: LocalDateTime
) {
    // Construtor padrão exigido pelo Hibernate
    constructor() : this(
        UUID.randomUUID(), "", "", "", UUID.randomUUID(), LocalDateTime.now()
    )
}
