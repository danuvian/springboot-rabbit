package daniel.rabbit.springbootrabbit;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringbootRabbitProducer extends Base {

    static final String QUEUE_NAME = "my-springboot-queue";

    @Autowired
    RabbitTemplate rabbitTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRabbitProducer.class, args);
    }

    /**
     * How to check if queue exists
     */
    @Bean
    CommandLineRunner queueExists() {
        return args -> {
            RabbitAdmin ra = new RabbitAdmin(rabbitTemplate);
            Properties prop = ra.getQueueProperties(QUEUE_NAME); 
            
            if(prop == null) {
                ra.declareQueue(new Queue(QUEUE_NAME));
                info("Queue '{}' does NOT exist", QUEUE_NAME);

                ra.declareQueue(new Queue(QUEUE_NAME));
                info("Queue '{}' created", QUEUE_NAME);
            }
            else {
                info("Queue '{}' DOES exist", QUEUE_NAME);
            }
        };
    }

    /*
        REST CALL(S)
    */
    @PostMapping(path="/msg", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> sendMessage(@RequestBody String jsonMsg) {
        info("received msg to send: {}", jsonMsg);
        rabbitTemplate.convertAndSend(QUEUE_NAME, jsonMsg);
        info("sent msg to rabbit [OK]");
        return ResponseEntity.ok().build();
    }
}