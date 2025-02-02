#!/bin/bash
set -e

until mongosh --port 5415 -u assignment-admin -p 'uF*3lviO0iaZcKTT479_t' --authenticationDatabase admin --eval "db.adminCommand('ping')" &>/dev/null; do
  echo "Waiting for MongoDB to be ready..."
  sleep 2
done

mongoimport --port 5415 -u assignment-admin -p 'uF*3lviO0iaZcKTT479_t' --authenticationDatabase admin --db dbank-assignment --collection customers --file /docker-entrypoint-initdb.d/customers.json --jsonArray || true
mongoimport --port 5415 -u assignment-admin -p 'uF*3lviO0iaZcKTT479_t' --authenticationDatabase admin --db dbank-assignment --collection branches --file /docker-entrypoint-initdb.d/branches.json --jsonArray || true

echo "Import process completed"