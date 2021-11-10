package org.decembrist.preprocessors.utils

import com.google.devtools.ksp.symbol.KSTypeReference

fun KSTypeReference.getClassName(): String = resolve().declaration.qualifiedName!!.asString()