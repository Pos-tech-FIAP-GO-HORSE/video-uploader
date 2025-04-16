package br.com.postech.videouploader.infra.persistence

import br.com.postech.videouploader.infra.persistence.entity.VideoEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface JpaVideoRepository : JpaRepository<VideoEntity, UUID>