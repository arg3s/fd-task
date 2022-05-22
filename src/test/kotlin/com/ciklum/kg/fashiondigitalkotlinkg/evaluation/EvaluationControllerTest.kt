package com.ciklum.kg.fashiondigitalkotlinkg.evaluation

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@WireMockTest(httpPort = 8089)
internal class EvaluationControllerTest {

    @Autowired
    lateinit var webClient: WebTestClient

    @Test
    fun `should return correct answer for reference file`() {
        //when
        aRequest("https://fid-recruiting.s3-eu-west-1.amazonaws.com/politics_en.csv")
            .let {
                //then
                it shouldNotBe null
                it.mostSpeeches shouldBe null
                it.mostSecurity shouldBe "Alexander Abel"
                it.leastWordy shouldBe "Caesare Collins"
            }

    }

    @Test
    fun `should return correct answer for file with speakers from 2013`() {
        //given
        stubFile("/file-with-speakers-from-2013.csv")
        //when
        aRequest("http://localhost:8089/file-with-speakers-from-2013.csv")
            .let {
                //then
                it shouldNotBe null
                it.mostSpeeches shouldBe "Alexander Abel"
                it.mostSecurity shouldBe "Alexander Abel"
                it.leastWordy shouldBe "Bernhard Belling"
            }
    }

    @Test
    fun `should handle duplicates correctly`() {
        //given
        stubFile("/file-with-duplicates.csv")
        //when
        aRequest("http://localhost:8089/file-with-duplicates.csv")
            .let {
                //then
                it shouldNotBe null
                it.mostSpeeches shouldBe null
                it.mostSecurity shouldBe "Alexander Abel"
                it.leastWordy shouldBe "Caesare Collins"
            }
    }


    @Test
    fun `should handle unambiguous results correctly`() {
        //given
        stubFile("/file-with-unambiguous-results.csv")
        //when
        aRequest("http://localhost:8089/file-with-unambiguous-results.csv")
            .let {
                //then
                it shouldNotBe null
                it.mostSpeeches shouldBe null
                it.mostSecurity shouldBe null
                it.leastWordy shouldBe null
            }
    }

    @Test
    fun `should handle multiple files`() {
        //given
        stubFile("/file-with-unambiguous-results.csv")
        stubFile("/file-with-speakers-from-2013.csv")
        stubFile("/reference-file.csv")
        //when
        aRequest(
            "http://localhost:8089/file-with-unambiguous-results.csv",
            "http://localhost:8089/file-with-speakers-from-2013.csv",
            "http://localhost:8089/reference-file.csv",
        ).let {
            //then
            it shouldNotBe null
            it.mostSpeeches shouldBe "Alexander Abel"
            it.mostSecurity shouldBe "Alexander Abel"
            it.leastWordy shouldBe "Bernhard Belling"
        }
    }

    private fun stubFile(vararg fileUrl: String) {
        fileUrl.forEach {
            stubFor(
                get(it).willReturn(ok().withBody(this::class.java.getResource(it)!!.readBytes()))
            )
        }
    }

    private fun aRequest(vararg url: String): EvaluationResult {
        return webClient.get().uri {
            it.path("/evaluation").queryParam("url", url.toList()).build()
        }.exchange().expectBody(EvaluationResult::class.java).returnResult().responseBody!!
    }
}