-- liquibase formatted sql

-- changeset Alexandr_Makutsevich:1
CREATE TABLE IF NOT EXISTS tasks (
   id BIGSERIAL PRIMARY KEY NOT NULL,
   name VARCHAR(55) NOT NULL,
   description TEXT,
   scheduled_date TIMESTAMP,
   priority VARCHAR(20) NOT NULL
);
-- rollback DROP TABLE IF EXISTS tasks;