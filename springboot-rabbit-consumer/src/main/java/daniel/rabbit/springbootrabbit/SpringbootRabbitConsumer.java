package daniel.rabbit.springbootrabbit;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;

@SpringBootApplication
@RabbitListener(queues = {"standalone-queue", "my-exchange-queue"})
public class SpringbootRabbitConsumer extends Base {

    @Autowired
    AmqpTemplate rabbitTemplate;

    ObjectMapper om = new ObjectMapper();

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRabbitConsumer.class, args);
    }

    @RabbitHandler
    void receive(String in, Message<String> msg) {
        debug("[x] Received headers = {}", msg.getHeaders());

        info("[x] Received from '{}': {}", msg.getHeaders().get("amqp_consumerQueue"), in);
    }
} 
