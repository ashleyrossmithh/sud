Информационная система для управления реестром судебных органов. Проект реализован в рамках лабораторной работы по дисциплине "Бэкенд разработка веб-приложения".

Стек технологий
Backend: Java 17, Spring Boot 3.4.0
Data: Spring Data JPA, Hibernate
Database: PostgreSQL / H2 (In-memory)
Security: Spring Security (Role-Based Access Control)
Testing: Postman

Основные функции
Полный CRUD: Управление записями судов (создание, чтение, обновление, удаление).
Безопасность: Доступ к методам защищен авторизацией (Роль: `ADMIN`).
Валидация: Проверка входных данных с возвратом кода `422 Unprocessable Entity`.
Обработка ошибок: Единый формат JSON-ответов для всех исключений.
Экспорт: Генерация реестра в формате Excel (.xlsx).

Установка и запуск

1. Склонируйте репозиторий:
   ```bash
   git clone https://github.com/ashleyrossmithh/sud.git
2. Соберите проект mvn clean install
3. Запустите приложение mvn spring-boot:run

Примеры API запросов 

GET, /api/entities/court, Получить список всех судов
POST, /api/entities/court, Создать новый суд (нужен JSON)
DELETE, /api/entities/court/{id}, Удалить запись по ID
POST, /api/entities/court/exportExcel, Скачать отчет в формате Excel
