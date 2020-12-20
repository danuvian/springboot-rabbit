# Spring Boot and RabbitMQ Examples

This repo demonstates how to use Spring Boot with RabbitMQ.

It is a multi-module maven project:

* `springboot-rabbit-consumer`: consumer for messages from rabbit - once received, it and displays the message on the console

* `springboot-rabbit-producer`: produces messages for rabbit - send messages to rabbit when it receives a POST request

The producer will create a queue (if it does not exist) called `my-springboot-queue` on the default exchange.
## How to run examples

### RabbitMQ using Docker

Install and run:

```
docker run -d --name my-rabbit rabbitmq:3-management
```

### Spring Boot Consumer

Compile and start the consumer service:

* `cd springboot-rabbit-consumer`

* `mvn spring-boot:run`

### Spring Boot Producer

Compile and start the producer service:

* `cd springboot-rabbit-producer`

* `mvn spring-boot:run`

### Send and Receive Message

To start sending messages, do a POST to the producer. Example:
```
curl -d '{"msg": "Hello World!"}' -H 'Content-Type: application/json' -X POST http://localhost:8080/msg
```

The **producer** will log to console:
```
received msg to send: {"msg": "Hello World!"}
sent msg to rabbit [OK]
```

The **consumer** will log to console:
```
[x] Received from 'my-springboot-queue': {"msg": "Hello World!"}
```

