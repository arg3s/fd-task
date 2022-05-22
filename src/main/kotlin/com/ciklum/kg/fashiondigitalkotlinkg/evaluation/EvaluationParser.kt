package com.ciklum.kg.fashiondigitalkotlinkg.evaluation

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import java.io.InputStream
import java.time.LocalDate

@Service
class EvaluationParser {
    private val csvReader = csvReader {}

    suspend fun parse(streamData: Collection<InputStream>): Collection<EvaluationData> = coroutineScope {
        streamData
            .map { async { parse(it) } }
            .awaitAll()
            .flatten()
    }

    private fun parse(inputStream: InputStream): Collection<EvaluationData> =
        csvReader.readAllWithHeader(inputStream)
            .map(::normalizeRow)
            .map(::toEvaluationData)

    private fun toEvaluationData(it: Map<String, String>) = EvaluationData(
        speaker = it["speaker"]!!,
        topic = it["topic"]!!,
        date = LocalDate.parse(it["date"]),
        words = it["words"]!!.toInt()
    )

    private fun normalizeRow(row: Map<String, String>) =
        row.map { it.key.lowercase().trim() to it.value.trim() }.toMap()

}
