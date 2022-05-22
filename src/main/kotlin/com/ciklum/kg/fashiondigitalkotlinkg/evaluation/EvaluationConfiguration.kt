package com.ciklum.kg.fashiondigitalkotlinkg.evaluation

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties(prefix = "evaluation.properties")
data class EvaluationConfigurationProperties(val codecsInMemorySize: Int, val timeoutMillis: Long)


@Configuration
class EvaluationConfiguration {

    @Bean
    fun evaluationWebClient(properties: EvaluationConfigurationProperties): WebClient = WebClient.builder()
        .exchangeStrategies(
            ExchangeStrategies
                .builder()
                .codecs { configurer: ClientCodecConfigurer ->
                    configurer.defaultCodecs().maxInMemorySize(properties.codecsInMemorySize)
                }
                .build()
        )
        .clientConnector(
            ReactorClientHttpConnector(
                HttpClient
                    .create()
                    .followRedirect(true)
                    .responseTimeout(Duration.ofMillis(properties.timeoutMillis))
            )
        )
        .build()
}
