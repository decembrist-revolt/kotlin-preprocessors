package org.decembrist.external

import org.decembrist.di.annotations.Injectable

// external library to inject
@Injectable
class ExternalService {
    val externalServiceMessage = "ExternalService"
}

@Injectable("external-string")
fun externalString() = "external"