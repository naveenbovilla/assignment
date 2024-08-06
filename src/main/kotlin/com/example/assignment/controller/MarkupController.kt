package com.example.assignment.controller

import com.example.assignment.service.MarkupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class MarkupController {

    @Autowired
    var markupService: MarkupService? = null

    @PostMapping("/create")
    fun generateHtml(@RequestBody inputMessage: String): ResponseEntity<String> {
        val returnValue = markupService?.convertStringToHtml(inputMessage)
        val headers: MultiValueMap<String, String> = HttpHeaders()
        headers.add("content-type", "text/html; charset=UTF-8")
        var responseEntity = ResponseEntity(returnValue!!, headers, HttpStatus.OK)
        return responseEntity
    }
}
