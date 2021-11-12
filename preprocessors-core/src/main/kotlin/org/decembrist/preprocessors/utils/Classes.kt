package org.decembrist.preprocessors.utils

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter

fun KSClassDeclaration.getPackageName(): String = packageName.asString()

fun KSClassDeclaration.getFullClassName(): String = qualifiedName!!.asString()

fun KSClassDeclaration.getPrimaryConstructorParameters(): List<KSValueParameter> =
    primaryConstructor?.parameters ?: emptyList()