# Spring Boot and RabbitMQ Examples

This repo demonstates how to use Spring Boot with RabbitMQ.

It is a multi-module maven project:

* `springboot-rabbit-consumer`: consumer for messages from rabbit - once message is received, it is displayed on the console

* `springboot-rabbit-producer`: produces messages for rabbit - send messages to rabbit when it receives a POST request

The producer will create a queue (if it does not exist) called `my-springboot-queue` on the default exchange.
## How to run examples

### RabbitMQ using Docker

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

The producer creates the following:

* Rabbit standalone queue: `standalone-queue`

* Rabbit exchange: `my-exchange`, queue: `my-exchange-queue`, routing key: `my-routing-key`

### Send and Receive Message

You can send messages in the following ways:

* Not specifying the exchange, routing key and queue name. This results in sending the message to the default queue: `standalone-queue`:
```
curl -d '{"msg": "Hello World!"}' -H 'Content-Type: application/json' -X POST http://localhost:8080/msg
```

* Specifying the queue name only. This reqults in sending the message to the specified queue:
```
curl -d '{"msg": "Hello World!"}' -H 'Content-Type: application/json' -X POST "http://localhost:8080/msg?queue=standalone-queue"
or
curl -d '{"msg": "Hello World!"}' -H 'Content-Type: application/json' -X POST "http://localhost:8080/msg?queue=my-exchange-queue"
```

* Specifying the exchange and the routing key. This results in sending the message to the specified exchange and routing key:
```
curl -d '{"msg": "Hello World!"}' -H 'Content-Type: application/json' -X POST "http://localhost:8080/msg?exchange=my-exchange&routingKey=crazy"
```

View messages sent in the producer console.

View message received in the consumer console.

### Change Rabbit Connection Settings

Change default Spring Boot rabbit connection setting(s) by adding the following value(s) to `application.properties`:

```
spring.rabbitmq.host=localhost # RabbitMQ host.
spring.rabbitmq.password= # Login to authenticate against the broker.
spring.rabbitmq.port=5672 # RabbitMQ port.
spring.rabbitmq.username= # Login user to authenticate to the broker.
```
