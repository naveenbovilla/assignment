package com.example.assignment.service

import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.StringReader
import java.util.*
import java.util.regex.Pattern


@Service
class MarkupService {

    @Autowired
    lateinit var mappingMap: Map<String,String>

    private val regex = Regex("(http|https|ftp)://")
    private val regex1 = Regex("[\\[](.*?)[)]")

    fun convertStringToHtml(inputMessage: String): String {

        var reader = BufferedReader(StringReader(inputMessage))

        //val scanner = Scanner(StringReader(inputMessage))
        val htmlStringBuilder = StringBuilder()
        var isTagClosed = true

        reader.forEachLine { line ->
            if (line.isNotEmpty()) {
                // construct p or h tag in this tempBuilder so that we can apply href before appending to final htmlStringBuilder
                val tempBuilder = StringBuilder()
                // Doesn't matter what the line starts with if there is link we need to construct href
                val hasLink = line.contains(regex) || line.contains(regex1)
                // Initialize tags as false
                var hTagElement = false
                // decide p or h tag
                if (line.startsWith("#")){
                    hTagElement = true
                }
                if(hTagElement){
                    //get #'s from the line
                    val hashStr = line.substring(0, line.indexOf(" "))
                    constructTagString(
                        tempBuilder,
                        // remove the #'s while constructing the tag
                        line.substring(line.indexOf(" ") + 1, line.length),
                        "h",
                        hashStr.length
                    )
                    tempBuilder.append("\n")
                } else {
                    // if isTagClosed is false means the previous element is p and next line is not empty,
                    // so we dont create new start p tag rather append the line to the builder.
                    if (isTagClosed) {
                        constructTagString(
                            tempBuilder,
                            line,
                            "p",
                            0
                        )
                    } else {
                        tempBuilder.append("---\n").append(line)
                    }
                    isTagClosed = false
                }
                if(hasLink){
                    var returnValue = constructHrefTag(tempBuilder.toString())
                    htmlStringBuilder.append(returnValue)
                } else {
                    htmlStringBuilder.append(tempBuilder.toString())
                }
            } else {
                // the open p tag in the above is close if new line which is empty is received
                if(!isTagClosed){
                    getHTMLTag(htmlStringBuilder,"p",0,true)
                    htmlStringBuilder.append("\n")
                    isTagClosed = true
                }
                htmlStringBuilder.append(line).append("\n")
            }
        }

        /** while (scanner.hasNextLine()) {
            val line: String = scanner.nextLine()
            if (line.isNotEmpty()) {
                val tempBuilder = StringBuilder()
                // Doesn't matter what the line starts with if there is link we need to construct href
                val hasLink = line.contains(regex) || line.contains(regex1)
                // Initialize tags as false
                var hTagElement = false
                // decide p or h tag
                if (line.startsWith("#")){
                    hTagElement = true
                }
                if(hTagElement){
                    val lineStart = line.substring(0, line.indexOf(" "))
                    constructTagString(
                        tempBuilder,
                        line.substring(line.indexOf(" ") + 1, line.length),
                        "h",
                        lineStart.length
                    )
                    tempBuilder.append("\n")
                } else {
                    if (isTagClosed) {
                        constructTagString(
                            tempBuilder,
                            line,
                            "p",
                            0
                        )
                    } else {
                        htmlString.append("\n").append(line)
                    }
                    isTagClosed = false
                }
                if(hasLink){
                    var returnValue = constructHrefTag(tempBuilder.toString())
                    htmlString.append(returnValue)
                } else {
                    htmlString.append(tempBuilder.toString())
                }
            } else {
                if(!isTagClosed){
                    getHTMLTag(htmlString,"p",0,true)
                    htmlString.append("\n")
                    isTagClosed = true
                }
                htmlString.append(line).append("\n")
            }
        } **/
        if(!isTagClosed){
            getHTMLTag(htmlStringBuilder,"p",0,true)
        }
        return htmlStringBuilder.toString()
    }

    fun constructTagString(htmlString: StringBuilder, line: String, htmlTag: String, length: Int) {
        getHTMLTag(htmlString, htmlTag,length, false)
        htmlString.append(line)
        // for h tag there is not need to check to close. Default it is closed. Will skip closing of p tag
        if("h" == htmlTag){
            getHTMLTag(htmlString, htmlTag, length, true)
        }
    }

    private fun getHTMLTag(htmlString: StringBuilder, htmlTag: String, length: Int, closeTag: Boolean) {
        htmlString.append('<')
        if(closeTag) {
            htmlString.append('/')
        }
        htmlString.append(htmlTag.takeIf { length == 0 } ?: (htmlTag + length))
        htmlString.append('>')
    }

    fun constructHrefTag(line: String): String {
        var returnValue = line
        val myMatcher = Pattern.compile(regex1.pattern)
            .matcher(line)
        var group: String? = null;
        while (myMatcher.find()) {
            group = myMatcher.group(0)
        }
        if (group != null) {
            var hrefText = StringUtils.substringBetween(group, "[", "](")
            var urlLink = StringUtils.substringBetween(group, "](", ")")
            var hrefBuilder = StringBuilder()
            hrefBuilder.append("<a href=\"")
            hrefBuilder.append(urlLink)
            hrefBuilder.append("\">")
            hrefBuilder.append(hrefText)
            hrefBuilder.append("</a>")
            returnValue = line.replace(group,hrefBuilder.toString())
        }
        return returnValue
    }

}