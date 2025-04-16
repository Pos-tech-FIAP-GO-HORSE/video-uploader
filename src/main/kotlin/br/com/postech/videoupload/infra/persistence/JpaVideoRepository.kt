package br.com.postech.videoupload.infra.persistence

import br.com.postech.videoupload.infra.persistence.entity.VideoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JpaVideoRepository : JpaRepository<VideoEntity, UUID>