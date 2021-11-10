package org.decembrist.preprocessors.utils

import com.google.devtools.ksp.symbol.*

fun KSNode.isTopLevel(): Boolean = parent?.let { it is KSFile } == true

fun KSModifierListOwner.findFirstModifierOrNull(vararg modifier: Modifier): Modifier? =
    modifier.firstOrNull { it in modifiers }