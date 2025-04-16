package br.com.postech.videoupload.domain.repository

import br.com.postech.videoupload.domain.model.Video
import java.util.*

interface VideoRepository {
    fun save(video: Video): Video
    fun findById(id: UUID): Video?
    fun findAll(): List<Video>
}