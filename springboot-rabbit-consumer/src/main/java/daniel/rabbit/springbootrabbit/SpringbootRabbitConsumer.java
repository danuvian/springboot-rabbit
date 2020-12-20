package daniel.rabbit.springbootrabbit;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RabbitListener(queues = {SpringbootRabbitConsumer.QUEUE_NAME})
public class SpringbootRabbitConsumer extends Base {

    static final String QUEUE_NAME = "my-springboot-queue";

    @Autowired
    AmqpTemplate rabbitTemplate;

    ObjectMapper om = new ObjectMapper();

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRabbitConsumer.class, args);
    }

    @RabbitHandler
    void receive(String in) {
        info("[x] Received from '{}': {}", QUEUE_NAME, in);
    }
}
