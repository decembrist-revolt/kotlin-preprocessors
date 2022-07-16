package org.decembrist.di

val classNameDictionary: Map<String, String?> = mapOf(
    "kotlin.collections.ArrayList" to java.util.ArrayList::class.qualifiedName,
    "kotlin.collections.LinkedHashMap" to java.util.LinkedHashMap::class.qualifiedName,
    "kotlin.collections.HashMap" to java.util.HashMap::class.qualifiedName,
    "kotlin.collections.LinkedHashSet" to java.util.LinkedHashSet::class.qualifiedName,
    "kotlin.collections.HashSet" to java.util.HashSet::class.qualifiedName,
)