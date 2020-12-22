package daniel.rabbit.springbootrabbit;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;

@SpringBootApplication
// @RabbitListener(queues = {"standalone-queue", "my-exchange-queue"})
@RabbitListener(queues = {"#{springbootRabbitConsumer.STANDALONE_QUEUE}", "#{springbootRabbitConsumer.EXCHANGE_QUEUE}"})
public class SpringbootRabbitConsumer extends Base {

    @Autowired
    AmqpTemplate rabbitTemplate;

    //
    // Listen for the following queues
    //
    @Value("${STANDALONE_QUEUE:standalone-queue}")
    public String STANDALONE_QUEUE;

    @Value("${EXCHANGE_QUEUE:my-exchange-queue}")
    public String EXCHANGE_QUEUE;
    
    public static void main(String[] args) {
        SpringApplication.run(SpringbootRabbitConsumer.class, args);
    }

    /**
     * Receive an incoming rabbit message.
     * @param in - the message
     * @param msg - message metadata object
     */
    @RabbitHandler()
    void receive(String in, Message<String> msg) {
        debug("[x] Received headers = {}", msg.getHeaders());

        info("[x] Received from '{}': {}", msg.getHeaders().get("amqp_consumerQueue"), in);
    }
} 
