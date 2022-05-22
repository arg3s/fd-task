package com.ciklum.kg.fashiondigitalkotlinkg.evaluation

import org.springframework.core.io.buffer.DataBuffer
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange
import java.io.InputStream
import java.net.URI
import java.time.LocalDate


data class EvaluationData(val speaker: String, val topic: String, val date: LocalDate, val words: Int)

@Service
class EvaluationDataProvider(
    private val evaluationWebClient: WebClient
) {

    suspend fun provide(urls: Collection<String>): Collection<InputStream> =
        urisFromUrls(urls)
            .map { download(it) }

    private fun urisFromUrls(urls: Collection<String>) = urls
        .toSet()
        .map { URI.create(it) }

    suspend fun download(uri: URI): InputStream = evaluationWebClient
        .get()
        .uri(uri)
        .awaitExchange {
            // lacks error handling - assuming availability of all uri resources as crucial to the evaluation process
            // and failure is accepted
            it.awaitBody<DataBuffer>().asInputStream()
        }

}
