package br.com.postech.videoupload.infra.persistence

import br.com.postech.videoupload.domain.model.Video
import br.com.postech.videoupload.infra.persistence.entity.VideoEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.*

class VideoRepositoryImplTest {

    private val jpaRepo = mock<JpaVideoRepository>()
    private val repo = VideoRepositoryImpl(jpaRepo)

    @Test
    fun `should save video and return domain object`() {
        val video = Video(
            id = UUID.randomUUID(),
            title = "title",
            description = "desc",
            url = "http://s3/video.mp4",
            createdAt = LocalDateTime.now(),
            userId = UUID.randomUUID()
        )

        val entity = VideoEntity(
            id = video.id,
            title = video.title,
            description = video.description,
            url = video.url!!,
            createdAt = video.createdAt,
            userId = video.userId
        )

        whenever(jpaRepo.save(any())).thenReturn(entity)

        val result = repo.save(video)

        assertEquals(video.id, result.id)
        assertEquals(video.url, result.url)
    }

    @Test
    fun `should find by id`() {
        val id = UUID.randomUUID()
        val entity = VideoEntity(
            id = id,
            title = "Title",
            description = "Desc",
            url = "url",
            createdAt = LocalDateTime.now(),
            userId = UUID.randomUUID()
        )

        whenever(jpaRepo.findById(id)).thenReturn(Optional.of(entity))

        val result = repo.findById(id)

        assertNotNull(result)
        assertEquals(entity.id, result!!.id)
    }

    @Test
    fun `should return all videos`() {
        val entities = listOf(
            VideoEntity(
                id = UUID.randomUUID(),
                title = "Title",
                description = "Desc",
                url = "url",
                createdAt = LocalDateTime.now(),
                userId = UUID.randomUUID()
            )
        )
        whenever(jpaRepo.findAll()).thenReturn(entities)

        val result = repo.findAll()

        assertEquals(1, result.size)
    }
}