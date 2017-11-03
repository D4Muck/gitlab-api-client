package at.d4m.gitlab.client.example

import org.slf4j.Logger

fun Logger.info(obj: Any) {
    this.info(obj.toString())
}