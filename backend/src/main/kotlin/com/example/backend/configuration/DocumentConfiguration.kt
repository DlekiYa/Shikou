package com.example.backend.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "document")
class DocumentConfiguration {
    var workspaceRootDirectory: String = ""
}