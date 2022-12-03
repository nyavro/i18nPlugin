package com.eny.i18n.plugin

enum class PrimitiveType {
    STRING, BOOL, NUMBER
}

data class ConfigurationProperty(
    val displayName: String,
    val primitiveType: PrimitiveType,
    val defaultValue: String,
    val nullable: Boolean,
    val id: String
)