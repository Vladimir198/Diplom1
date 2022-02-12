# Automatization Final Project

## О проекте
В рамках данного проекта необходимо автоматизировать тестирование веб-сервиса покупки тура, взаимодействующего с СУБД и API Банка.

Полные условия и исходные данные можно посмотреть [здесь](https://github.com/netology-code/qa-diploma).

## Документация:

[План по автоматизации тестирования](https://github.com/volontare/AQA-Diploma/blob/master/documents/Plan.md)

[Отчет по итогам автоматизированного тестирования](https://github.com/volontare/AQA-Diploma/blob/master/documents/Report.md)

[Отчет по итогам автоматизации](https://github.com/volontare/AQA-Diploma/blob/master/documents/Summary.md)


## Необходимое окружение: 
 * Docker Desktop. Скачать и ознакомиться с документацией можно [здесь](https://www.docker.com/products/docker-desktop); 

 * AdoptOpenJDK 11.0.11+9. Скачать и ознакомиться с документацией можно [здесь](https://adoptopenjdk.net/index.html);

 * IntelliJ Idea. Скачать и ознакомиться с документацией можно [здесь](https://www.jetbrains.com/ru-ru/idea/).


## Запуск тестов

 Перед запуском тестов необходимо убедиться, что  порты  8080, 9999 и 5432 или 3306 (в зависимости от выбранной БД) свободны; 

1. Склонируйте репозиторий, введя команду git clone https://github.com/volontare/AQA-Diploma.git в Git Bash или воспользуйтесь VCS Git, встроенной в IntelliJ IDEA.

2. Откройте проект в IntelliJ IDEA

3. В терминале введите команду `docker-compose up -d`. Параметры для запуска хранятся в файле `docker-compose.yml`

4. Приложение запускается на порту 8080, по умолчанию используется БД MySQL

При необходимости изменения порта замените `sut.url` в `build.gradle` на `systemProperty 'sut.url', System.getProperty('sut.url', 'http://localhost:8090')`

Для изменения БД замените `db.url` на `systemProperty 'db.url', System.getProperty('db.url', 'jdbc:postgresql://localhost:5432/app')`

В новой вкладке терминала введите команду:

- `java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app -jar artifacts/aqa-shop.jar` - для БД MySQL

- `java -Dspring.datasource-postgresql.url=jdbc:postgresql://localhost:5432/app -jar artifacts/aqa-shop.jar` - для БД PostgreSQL

При изменении порта для запуска тестов необходимо указать: 

- `java -Dserver.port=8090 -Dspring.datasource.url=jdbc:mysql://localhost:3306/app -jar artifacts/aqa-shop.jar` - для БД MySQL

- `java -Dserver.port=8090 -Dspring.datasource-postgresql.url=jdbc:postgresql://localhost:5432/app -jar artifacts/aqa-shop.jar` - для БД PostgreSQL

5. В новой вкладке терминала введите команду в зависимости от запущенной ранее БД:
- `gradlew clean test -Ddb.url=jdbc:mysql://localhost:3306/app` - для БД MySQL

- `gradlew clean test -Ddb.url=jdbc:postgresql://localhost:5432/app` - для БД PostgreSQL


## Подготовка отчета Allure
При необходимости создания отчета тестирования, запустите тесты следующим образом:
- `gradlew clean test -Ddb.url=jdbc:mysql://localhost:3306/app allureReport` - для БД MysSQL

- `gradlew clean test -Ddb.url=jdbc:postgresql://localhost:5432/app allureReport` - для БД Postgresql

`allureReport` - используется при первой генерации отчета. 

При повторной генерации отчета необходимо запускать тесты командой:
- `gradlew test -Ddb.url=jdbc:mysql://localhost:3306/app allureServe` - для БД MysSQL

- `gradlew test -Ddb.url=jdbc:postgresql://localhost:5432/app allureServe` - для БД Postgresql

Отчет открывается после прохождения тестов автоматически в браузере по умолчанию.

Если потребуется преждевременно завершить прохождение тестов, наберите команду Ctrl+C, далее Y. ДЛя остановки контейнеров необходимо ввести команду `docker-compose down`
