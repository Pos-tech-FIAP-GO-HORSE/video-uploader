package br.com.postech.videoupload.infra.persistence

import br.com.postech.videoupload.infra.persistence.entity.VideoEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface JpaVideoRepository : JpaRepository<VideoEntity, UUID>