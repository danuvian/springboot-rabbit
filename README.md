# Spring Boot and RabbitMQ Examples

This repo demonstates how to use Spring Boot with RabbitMQ.

It is a multi-module maven project:

* `springboot-rabbit-consumer`: consumer for messages from rabbit - once message is received, it is displayed on the console

* `springboot-rabbit-producer`: produces messages for rabbit - send messages to rabbit when it receives a POST request

The producer will create a queue (if it does not exist) called `my-springboot-queue` on the default exchange.
## How to run examples

### RabbitMQ using Dockerds

Install and run:

```
docker run -d --name my-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

### Spring Boot Consumer

Start the consumer service:

* `cd springboot-rabbit-consumer`

* `mvn spring-boot:run`

### Spring Boot Producer

Start the producer service:

* `cd springboot-rabbit-producer`

* `mvn spring-boot:run`

### Send and Receive Message

To start sending messages, do a POST call to the producer. Example:
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

