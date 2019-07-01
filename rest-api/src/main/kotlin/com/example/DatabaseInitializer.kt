package com.example

import com.example.util.sql.toSqlTimestamp
import mu.KLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class DatabaseInitializer(val tweeterRepo: TweeterRepo) : CommandLineRunner {

    override fun run(vararg args: String) {
        logger.info { "START DB INITIALIZER" }
        logger.info { "x=$x" }


    }

    companion object : KLogging()
}
