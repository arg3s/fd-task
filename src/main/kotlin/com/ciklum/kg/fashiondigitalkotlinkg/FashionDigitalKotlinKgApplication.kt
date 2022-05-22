package com.ciklum.kg.fashiondigitalkotlinkg

import com.ciklum.kg.fashiondigitalkotlinkg.evaluation.EvaluationConfigurationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
@EnableConfigurationProperties(EvaluationConfigurationProperties::class)
class FashionDigitalKotlinKgApplication

fun main(args: Array<String>) {
    runApplication<FashionDigitalKotlinKgApplication>(*args)
}
