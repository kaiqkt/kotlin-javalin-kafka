package com.kaique.resources.repositories

import com.kaique.domain.entities.Event
import com.kaique.domain.gateways.EventRepository
import com.kaique.resources.repositories.documents.EventDocument
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EventMongoRepository(
    mongo: MongoDatabase,
    collectionName: String
): EventRepository {
    private val collection: MongoCollection<EventDocument> = mongo.getCollection(
        collectionName,
        EventDocument::class.java
    )

    override fun create(event: Event<String>) {
        val document = EventDocument.fromTransfer(event)
        collection.insertOne(document).also {
            log.info("Event with id ${document._id} persisted successfully")
        }
    }

    private val log: Logger = LoggerFactory.getLogger(javaClass)
}