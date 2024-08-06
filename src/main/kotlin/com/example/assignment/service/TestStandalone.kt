package com.example.assignment.service

import java.io.File

fun main(args: Array<String>) {
    var service = MarkupService()
    var htmlString = service.convertStringToHtml(File("src/main/resources/input.txt").readText())
    println(htmlString)
}