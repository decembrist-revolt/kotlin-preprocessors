package org.decembrist.preprocessors.utils

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

fun KSFunctionDeclaration.getFullName() = (qualifiedName ?: simpleName).asString()

fun KSFunctionDeclaration.getSimpleName() = simpleName.asString()

fun KSFunctionDeclaration.getParentClassName() = (parent as KSClassDeclaration).qualifiedName!!.asString()