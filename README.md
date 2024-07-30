# Files Uploader

### Репозиторий

```
git clone git@github.com:aaogoltcov/JavaDiplomaFilesUpload.git
```

### Запуск проекта

```
cd JavaDiplomaFilesUpload
docker-compose up
````

Приложение будет запущено на http://localhost:8000. Для работы с frontend приложением требуется создать пользователя.

```
POST http://localhost:8080/sign-up

{
    "login": "user",
    "email": "user@user.ru",
    "password": "user"
}
````
