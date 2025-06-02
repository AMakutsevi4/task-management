-- liquibase formatted sql

-- changeset Alexandr_Makutsevich:3
CREATE TABLE IF NOT EXISTS tags (
   id BIGSERIAL PRIMARY KEY NOT NULL,
   name VARCHAR(50) NOT NULL UNIQUE
    );

-- rollback DROP TABLE IF EXISTS tags;

-- changeset Alexandr_Makutsevich:4
CREATE TABLE IF NOT EXISTS task_tags (
    task_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (task_id, tag_id),
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
    );