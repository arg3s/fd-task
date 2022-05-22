package com.ciklum.kg.fashiondigitalkotlinkg.evaluation

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

class EvaluationCalculationResult(
    val mostSpeeches: MutableMap<String, Int> = ConcurrentHashMap(),
    val mostSecurity: MutableMap<String, Int> = ConcurrentHashMap(),
    val leastWordy: MutableMap<String, Int> = ConcurrentHashMap()
) {
    private val comparator = Comparator.comparing<Pair<String, Int>?, Int?> { pair -> pair.second }
    private val reversedComparator = comparator.reversed()
    fun getMostSpeeches(): String? {
        return getTheMostValueFrom(mostSpeeches, reversedComparator)
    }

    fun getMostSecurity(): String? {
        return getTheMostValueFrom(mostSecurity, reversedComparator)
    }

    fun getLeastWordy(): String? {
        return getTheMostValueFrom(leastWordy, comparator)
    }

    private fun getTheMostValueFrom(
        map: MutableMap<String, Int>,
        comparator: Comparator<Pair<String, Int>>
    ): String? {
        if (map.isEmpty()) {
            return null
        }
        val sortedValues = map.toList().sortedWith(comparator)
        val iterator = sortedValues.iterator()
        val firstPair = iterator.next()
        return when {
            !iterator.hasNext() -> firstPair.first
            firstPair.second != iterator.next().second -> firstPair.first
            else -> null
        }
    }
}

@Service
class EvaluationCalculator {

    fun calculate(evaluationData: Collection<EvaluationData>): EvaluationCalculationResult {
        return evaluationData.fold(initial = EvaluationCalculationResult()) { acc: EvaluationCalculationResult, model ->
            if (model.date.year == 2013) {
                acc.mostSpeeches.merge(model.speaker, 1) { prev, _ -> prev + 1 }
            }
            if (model.topic.lowercase() == "internal security") {
                acc.mostSecurity.merge(model.speaker, 1) { prev, _ -> prev + 1 }
            }
            acc.leastWordy.merge(model.speaker, model.words) { prev, curr -> prev + curr }
            acc
        }
    }

}
