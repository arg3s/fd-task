package com.ciklum.kg.fashiondigitalkotlinkg.evaluation

import org.springframework.stereotype.Service


data class EvaluationResult(
    val mostSpeeches: String? = null,
    val mostSecurity: String? = null,
    val leastWordy: String? = null
)


@Service
class EvaluationService(
    private val provider: EvaluationDataProvider,
    private val parser: EvaluationParser,
    private val calculator: EvaluationCalculator
) {

    suspend fun evaluate(urls: Collection<String>): EvaluationResult {
        // lacks URL validation, assuming urls to be always correct
        return provider.provide(urls)
            .run { parser.parse(this) }
            .run { calculator.calculate(this) }
            .run { toEvaluationResult() }
    }

    private fun EvaluationCalculationResult.toEvaluationResult() =
        EvaluationResult(
            mostSpeeches = getMostSpeeches(),
            mostSecurity = getMostSecurity(),
            leastWordy = getLeastWordy()
        )
}
