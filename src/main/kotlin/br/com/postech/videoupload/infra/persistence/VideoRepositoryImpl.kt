package br.com.postech.videoupload.infra.persistence

import br.com.postech.videoupload.domain.model.Video
import br.com.postech.videoupload.domain.repository.VideoRepository
import br.com.postech.videoupload.infra.persistence.entity.VideoEntity
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class VideoRepositoryImpl(
    private val jpaVideoRepository: JpaVideoRepository
) : VideoRepository {

    override fun save(video: Video): Video {
        val entity = VideoEntity(
            id = video.id,
            title = video.title,
            description = video.description,
            url = video.url,
            createdAt = video.createdAt,
            userId = video.userId
        )
        val savedEntity = jpaVideoRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findById(id: UUID): Video? {
        return jpaVideoRepository.findById(id).orElse(null)?.toDomain()
    }

    override fun findAll(): List<Video> {
        return jpaVideoRepository.findAll().map { it.toDomain() }
    }

    private fun VideoEntity.toDomain(): Video {
        return Video(
            id = this.id,
            title = this.title,
            description = this.description,
            url = this.url,
            createdAt = this.createdAt,
            userId = this.userId
        )
    }
}