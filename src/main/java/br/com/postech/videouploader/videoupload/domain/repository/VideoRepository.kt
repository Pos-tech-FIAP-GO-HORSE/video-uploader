package br.com.postech.videouploader.videoupload.domain.repository

import br.com.postech.videouploader.videoupload.domain.model.Video
import java.util.*

interface VideoRepository {
    fun save(video: Video): Video
    fun findById(id: UUID): Video?
    fun findAll(): List<Video>
}