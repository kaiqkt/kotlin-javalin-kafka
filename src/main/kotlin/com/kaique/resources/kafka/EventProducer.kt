package com.kaique.resources.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.kaique.domain.entities.Event
import io.azam.ulidj.ULID
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.header.internals.RecordHeader
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class EventProducer(
    private val kafkaUrl: String,
    private val topic: String,
    private val objectMapper: ObjectMapper
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    inline fun <reified T> emit(event: Event<T>) = emit(event, T::class.java)

        fun <T> emit(event: Event<T>, clazz: Class<T>) {
        val record = getProducerRecord(event, event.correlationId, clazz)

        createProducer().apply {
            send(record) { data, ex ->
                if (ex != null) {
                    log.error("Error to send event ${record.key()} error: ${ex.message}")
                    return@send
                }
                log.info("Send event ${record.key()} to topic ${data.topic()} at timestamp ${data.timestamp()}")
            }.get()
        }
    }

    private fun <T> getProducerRecord(event: Event<T>, correlationId: String?, clazz: Class<T>): ProducerRecord<String, String> {
        val serializedEvent = objectMapper.writeValueAsString(event)
        val eventTypeHeader = RecordHeader("eventType", objectMapper.writeValueAsBytes(clazz))
        val headers = listOf<Header>(eventTypeHeader)

        return ProducerRecord(
            topic,
            null,
            correlationId ?: ULID.random(),
            serializedEvent,
            headers
        )
    }

    private fun createProducer(): KafkaProducer<String, String> {
        val props = Properties()
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl)
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
        props.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")
        props.setProperty(ProducerConfig.ACKS_CONFIG, "all")
        props.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5")
        props.setProperty(ProducerConfig.RETRIES_CONFIG, "10")
        return KafkaProducer<String, String>(props)
    }
}