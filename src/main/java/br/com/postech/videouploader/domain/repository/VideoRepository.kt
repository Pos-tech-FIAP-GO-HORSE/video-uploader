package br.com.postech.videouploader.domain.repository

import br.com.postech.videouploader.domain.model.Video
import java.util.*

interface VideoRepository {
    fun save(video: Video): Video
    fun findById(id: UUID): Video?
    fun findAll(): List<Video>
}