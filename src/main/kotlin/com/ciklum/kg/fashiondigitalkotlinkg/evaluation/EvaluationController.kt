package com.ciklum.kg.fashiondigitalkotlinkg.evaluation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/evaluation")
class EvaluationController(private val service: EvaluationService) {

    @GetMapping
    suspend fun evaluate(@RequestParam(name = "url", required = false) urls: Collection<String>) =
        service.evaluate(urls)
}

