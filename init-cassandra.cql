CREATE KEYSPACE IF NOT EXISTS ecommerce 
WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};

USE ecommerce;

CREATE TABLE IF NOT EXISTS user_activity (
    id uuid PRIMARY KEY,
    userId text,
    activityType text,
    metadata text,
    timestamp timestamp
);
