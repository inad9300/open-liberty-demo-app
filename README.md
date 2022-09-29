```sh
docker-compose up --force-recreate --build --remove-orphans

docker exec --interactive --tty demo-app_demo-app_1 bash

top

./mvnw clean compile liberty:dev

watch touch src/main/java/com/demo/rest/SseController.java
```
