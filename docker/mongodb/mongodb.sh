#!/usr/bin/env bash

echo 'Creating application user and db'

mongo event-service \
        --host localhost:27017 \
        --authenticationDatabase admin \
        --eval "db.createUser({user: 'event-service', pwd: 'event-service', roles:[{role:'dbOwner', db: 'event-service'}]});"