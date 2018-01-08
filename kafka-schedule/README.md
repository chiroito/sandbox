5 秒毎にメッセージを作成し、Binder(Kafka) へ送信して、Sink がメッセージを標準出力へ出力する

Build Spring Boot Application
```bash
mvnw clean package -DskipTests=true
```

Run Kafka on Docker
```bash
docker run -d --name kafka -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=127.0.0.1 --env ADVERTISED_PORT=9092 spotify/kafka
```

Run Spring Boot Application
```bash
java -jar target/schedule-0.0.1-SNAPSHOT.jar
```