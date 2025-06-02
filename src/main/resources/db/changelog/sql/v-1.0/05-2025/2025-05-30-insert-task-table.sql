-- liquibase formatted sql

-- changeset Alexandr_Makutsevich:2
INSERT INTO tasks(name, description, scheduled_date, priority)
VALUES ('Сдать отчёт', 'Необходимо сдать отчёт по проекту', '2025-06-01 10:00:00', 'IMPORTANT'),
       ('Позвонить клиенту', 'Уточнить требования по задаче', '2025-06-03 14:00:00', 'USUAL'),
       ('Исправить баг', 'Баг в сервисе, срочно править', '2025-05-28 09:00:00', 'URGENT');