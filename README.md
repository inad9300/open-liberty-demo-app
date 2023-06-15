```sh
docker compose up --force-recreate --build --remove-orphans

docker compose exec -it demo-app bash

top -o RES -d 1

./mvnw clean compile liberty:dev

watch --interval 1 touch src/main/java/com/demo/rest/SseController.java
```
