package com.kaique.application.web.broker.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.kaique.application.web.broker.entities.Event
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*

class EventConsumer(
    private val kafkaUrl: String,
    private val topic: String,
    private val objectMapper: ObjectMapper
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    inline fun <reified T> consume(noinline service: (T) -> Unit) = consume(T::class.java, service)

    fun <T> consume(clazz: Class<T>, service: (T) -> Unit) {
        createConsumer().apply {
            subscribe(listOf(topic))

            while (true) {
                val records = poll(Duration.ofSeconds(0))

                records.iterator().forEach {
                    val event = objectMapper.readValue(it.value(), clazz)
                    service(event)
                    log.info("Consumed event ${String(it.headers().first().value())} from topic ${it.topic()}")
                }
            }
        }
    }

    private fun createConsumer(): Consumer<String, String> {
        val props = Properties()
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl)
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
        props.setProperty(
            ConsumerConfig.GROUP_ID_CONFIG,
            EventConsumer::class.java.simpleName
        ) // id do grupo que do qual pertence um consumer
        props.setProperty(
            ConsumerConfig.CLIENT_ID_CONFIG,
            EventConsumer::class.java.simpleName + "-" + UUID.randomUUID().toString()
        ) // assinar o consumer
        props.setProperty(
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
            "1"
        ) // definir quantas mensagens vao ser consumidas por poll
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest") // definir em qual comecar
        return KafkaConsumer(props)
    }
}