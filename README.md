# Hotel System

REST API для управления гостиницей: заселение и выселение клиентов, бронирование номеров, заказ услуг, управление персоналом.

## Технологии

- **Java 17**, Spring Boot 3.4.2
- **Spring Security** + JWT (аутентификация и авторизация)
- **Spring Data JPA** + Hibernate
- **PostgreSQL** + Liquibase (миграции)
- **MapStruct** + Lombok
- **Docker** / docker-compose

## Быстрый старт

### Требования

- Java 17+
- Maven 3.8+
- Docker & docker-compose (или PostgreSQL локально)

### Запуск через Docker

```bash
git clone https://github.com/R3vend3r/Senla-Course.git
cd Senla-Course
git checkout main_code
docker-compose up --build
```

Приложение будет доступно на `http://localhost:8080`.

### Запуск локально

1. Создать базу данных PostgreSQL:

```bash
psql -U postgres -f DB/init_db.sql
psql -U postgres -d hotel_db -f DB/test_data.sql
```

2. Настроить подключение в `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hotel_db
spring.datasource.username=postgres
spring.datasource.password=your_password
jwt.secret=your_jwt_secret
```

3. Собрать и запустить:

```bash
mvn clean package -DskipTests
java -jar target/hotel-system.jar
```

## Аутентификация

Все эндпоинты (кроме `/auth/**`) требуют JWT-токен в заголовке:

```
Authorization: Bearer <token>
```

### Регистрация

```http
POST /auth/register
Content-Type: application/json

{
  "login": "admin",
  "password": "secret",
  "role": "ADMIN"
}
```

### Вход

```http
POST /auth/login
Content-Type: application/json

{
  "login": "admin",
  "password": "secret"
}
```

Ответ: `{ "token": "eyJ..." }`

## API

### Номера (`/api/rooms`)

| Метод | Путь | Роль | Описание |
|-------|------|------|----------|
| GET | `/api/rooms` | USER, ADMIN | Список номеров (параметры: `sortType`, `onlyAvailable`) |
| GET | `/api/rooms/{number}` | USER, ADMIN | Найти номер |
| GET | `/api/rooms/available` | USER, ADMIN | Доступные номера на дату (`date=dd-MM-yy`) |
| GET | `/api/rooms/available/count` | USER, ADMIN | Количество свободных номеров |
| GET | `/api/rooms/{number}/available` | USER, ADMIN | Проверить доступность номера |
| POST | `/api/rooms` | ADMIN | Добавить номер |
| PATCH | `/api/rooms/{number}/status` | ADMIN | Изменить статус номера |
| PATCH | `/api/rooms/{number}/price` | ADMIN | Изменить цену номера |

Параметры сортировки (`sortType`): `PRICE`, `CAPACITY`, `STARS`, `TYPE`, `NONE`

### Клиенты (`/api/clients`)

| Метод | Путь | Роль | Описание |
|-------|------|------|----------|
| GET | `/api/clients` | ADMIN | Список всех клиентов |
| GET | `/api/clients/id/{clientId}` | USER, ADMIN | Найти по ID |
| GET | `/api/clients/search?name=&surname=` | USER, ADMIN | Найти по имени и фамилии |
| GET | `/api/clients/room/{roomNumber}` | USER, ADMIN | Клиент в номере |
| GET | `/api/clients/count` | USER, ADMIN | Количество клиентов |
| POST | `/api/clients` | ADMIN | Зарегистрировать клиента |

### Услуги (`/api/amenities`)

| Метод | Путь | Роль | Описание |
|-------|------|------|----------|
| GET | `/api/amenities` | USER, ADMIN | Список услуг (параметр: `sortType`) |
| GET | `/api/amenities/id/{id}` | USER, ADMIN | Найти по ID |
| GET | `/api/amenities/name/{name}` | USER, ADMIN | Найти по названию |
| POST | `/api/amenities` | ADMIN | Добавить услугу |
| PATCH | `/api/amenities/{name}/price` | ADMIN | Изменить цену услуги |

### Заказы и заселение (`/api/orders`)

| Метод | Путь | Роль | Описание |
|-------|------|------|----------|
| POST | `/api/orders/settle` | USER, ADMIN | Заселить клиента |
| POST | `/api/orders/evict/{roomNumber}` | USER, ADMIN | Выселить клиента |
| POST | `/api/orders/amenities` | USER, ADMIN | Добавить услугу к бронированию |
| GET | `/api/orders/bookings/active` | USER, ADMIN | Активные бронирования |
| GET | `/api/orders/bookings/completed` | USER, ADMIN | Завершённые бронирования |
| GET | `/api/orders/bookings/room/{number}/last` | USER, ADMIN | Последние 3 бронирования номера |
| GET | `/api/orders/history/room/{number}` | USER, ADMIN | История клиентов в номере |
| GET | `/api/orders/amenities/client/{clientId}` | USER, ADMIN | Услуги клиента |
| GET | `/api/orders/payment/room/{number}` | USER, ADMIN | Итоговая стоимость для номера |
| GET | `/api/orders/revenue/total` | ADMIN | Общая выручка |

**Пример заселения:**
```json
POST /api/orders/settle
{
  "clientId": "CL-abc12345",
  "roomNumber": 101,
  "checkOutDate": "2025-12-31T00:00:00.000+00:00"
}
```

### Бронирования (`/api/bookings`)

| Метод | Путь | Роль | Описание |
|-------|------|------|----------|
| GET | `/api/bookings/active/room/{number}` | USER, ADMIN | Активное бронирование номера |
| GET | `/api/bookings/active/client/{clientId}` | USER, ADMIN | Активное бронирование клиента |
| GET | `/api/bookings/client/by-room/{number}` | USER, ADMIN | Клиент по номеру |
| GET | `/api/bookings/room/by-client/{clientId}` | USER, ADMIN | Номер по клиенту |

## Структура проекта

```
src/
├── main/java/hotel_system/
│   ├── controller/          # REST-контроллеры + GlobalExceptionHandler
│   ├── service/
│   │   ├── entityService/   # Бизнес-логика (Room, Client, Booking, Amenity, Order)
│   │   ├── csv/             # Импорт/экспорт CSV
│   │   └── AuthService.java
│   ├── dao/                 # Слой доступа к данным (Hibernate)
│   ├── model/
│   │   ├── entity/          # JPA-сущности
│   │   └── mapper/          # MapStruct маперы
│   ├── dto/                 # Request/Response объекты
│   ├── security/            # JWT фильтры и обработчики
│   ├── Utils/               # SecurityConfig, LiquibaseConfig, HotelConfig
│   └── enums/               # RoomType, RoomCondition, SortType, Role
├── resources/
│   ├── application.properties
│   └── changelog/           # Liquibase миграции
DB/
├── init_db.sql              # Создание схемы
└── test_data.sql            # Тестовые данные
```

## Тесты

```bash
mvn test
```

Тесты охватывают все сервисы и контроллеры с использованием Mockito.

## Конфигурация

Параметры в `application.properties`:

| Параметр | По умолчанию | Описание |
|----------|-------------|----------|
| `hotel.room.status.change.enabled` | `true` | Разрешить изменение статуса номера |
| `hotel.auto.save.enabled` | `true` | Автосохранение |
| `jwt.expiration-ms` | `86400000` | Время жизни JWT (24 часа) |

Формат даты в запросах: `dd-MM-yy` (например, `31-12-25`).
