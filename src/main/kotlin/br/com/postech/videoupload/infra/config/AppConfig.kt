package br.com.postech.videoupload.infra.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories("br.com.postech.videoupload.infra.persistence")
@EntityScan("br.com.postech.videoupload.infra.persistence.entity")
open class AppConfig