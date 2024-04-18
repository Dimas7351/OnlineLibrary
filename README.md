## Проект цифровизации библиотеки с разделениям по ролям

В данном проекте присутствуют две роли: user (читатель) и admin (библиотекарь). Пользователь может брать и возвращать книги, а также просматривать все доступные книги. Библиотекарю дозволено проводить операции Read, Update, Delete для книг и пользователей.


Стек:
Spring Framework (core, security, boot), JPA, Hibernate ORM.

В проекте есть следующие структурные единицы:
* controllers
* mappers
* models
* views (templates)
* repositories
* services
* config
* dto (soon)

### Запуск программы

* 1 способ: запуск boot приложения через IDE
* 2 способ:
    - ```mvnv package```
    - ```cd target```
    - ```java -jar *name*```
