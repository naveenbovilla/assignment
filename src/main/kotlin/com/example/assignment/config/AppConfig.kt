package com.example.assignment.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AppConfig {

    @Bean
    fun mappingMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["#"] = "h1";
        map["##"] = "h2";
        map["###"] = "h3";
        map["####"] = "h4";
        map["#####"] = "h5";
        map["######"] = "h6";
        map["link"] = "a";
        map["text"] = "p";
        return map
    }
}