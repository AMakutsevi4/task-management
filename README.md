# Task Management Java Backend

[![Java 17](https://img.shields.io/badge/java-17-e5e7eb?style=for-the-badge&logo=openjdk&logoColor=black&labelColor=f3f4f6)](https://openjdk.java.net/projects/jdk/17/)
[![PostgreSQL 16](https://img.shields.io/badge/postgresql-16-e5e7eb?style=for-the-badge&logo=postgresql&logoColor=black&labelColor=f3f4f6)](https://www.postgresql.org/about/news/postgresql-16-released-2526/)
[![Spring Boot 3.4.0](https://img.shields.io/badge/springboot-3.4.0-e5e7eb?style=for-the-badge&logo=springboot&logoColor=black&labelColor=f3f4f6)](https://spring.io/projects/spring-boot)

# Разворачивание проекта

**Локальный запуск приложения без использования `docker-compose.yml`**

- Для конфигурации TaskApplication откройте Run -> Edit Configurations...
- Установите Active profile `local`
- Apply -> OK



**Локальный запуск приложения с использованием `docker-compose.yml`**

---

### 1. Склонировать репозиторий

```bash
git clone git@github.com:AMakutsevi4/task-management.git
```

---

### 2. Запуск контейнеров

Запустите все сервисы, указанные в `docker-compose.yml`:

```bash
docker-compose -f docker-compose.yml up
```

---

### 3. Проверка работы

1. Убедитесь, что PostgreSQL запущен и доступен:
   ```bash
   docker ps
   ```
   Контейнер `postgres` должен быть в списке запущенных, а порт `5432` (или указанный в `${SERVER_PORT}`) должен
   быть проброшен.

2. Проверьте, что Swagger UI доступен по адресу [https://localhost:8443/swagger-ui/index.htmll](http://localhost:8443).

---

### 4. Остановка контейнеров

Для остановки всех запущенных контейнеров выполните:

```bash
docker-compose -f docker-compose.yml down
```

---

### 5. Рекомендации

- **Очистка данных PostgreSQL**: Если нужно сбросить базу данных, удалите volume:
  ```bash
  docker volume rm task_pg_task
  ```

---

# Работа с сервисом

- Методы `GET` доступны без авторизации.
- Для остальных методов сервис использует **Basic Auth** с двумя пользователями в памяти:

| Логин  | Пароль | Роль   |
|--------|--------|--------|
| admin  | admin  | ADMIN  |
| user   | user   | USER   |