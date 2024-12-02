# Данный проект является серверной частью для [TradingCompanyApp](https://github.com/mvshvetsov/TradingCompanyApp)
___
## Основные функции
1. Регистрация работника
2. Авторизация работника
3. Функции работы с сотрудниками, если у пользователя роль директора
   + Добавление
   + Изменение информации
   + Удаление
   + Поиск
4. Функции работы с товарами, закупками, клиентами, заказами
   + Добавление
   + Изменение информации
   + Удаление
   + Поиск
___
## Используемые технологии
+ ![Static Badge](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&color=white)
+ ![Static Badge](https://img.shields.io/badge/-Ktor-087CFA?style=for-the-badge&logo=Ktor&logoColor=white)
+ ![Static Badge](https://img.shields.io/badge/kotlinx%20serialization-7F52FF?style=for-the-badge&logoColor=white)
+ ![Static Badge](https://img.shields.io/badge/-MySql-4479A1?style=for-the-badge&logo=mysql&color=white&logoSize=auto)
___
## Установка
1. Склонировать репозиторий
   + С помощью командной строки перейти в папку, где будет храниться проект
   ```
   cd C:\папка
   ```
   + Склонировать проект
   ```
   git init
   git clone https://github.com/mvshvetsov/trading_company_backend
   ```
2. Открыть проект в редакторе кода
3. В папке resources создать файл application.yaml
   + Настройка файла
     ```yaml
     ktor:
       application:
           modules:
               - com.example.ApplicationKt.module
       deployment:
           host: "Ваш адрес сервера"
           port: Порт
     database:
        dbUrl: "Ссылка на вашу базу данных"
        dbUser: "Имя пользователя базы данных"
        dbPassword: "Пароль для пользователя базы данных"
     jwt:
        domain: "URL вашего сервера авторизации или стороннего провайдера токенов. Пример: https://auth.example.com/"
        audience: "Аудитория токена. Пример: your-app-audience"
        realm: "Область аутентификации. Пример: example-realm"
     ``` 
