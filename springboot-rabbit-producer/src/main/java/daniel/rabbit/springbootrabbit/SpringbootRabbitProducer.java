package daniel.rabbit.springbootrabbit;

import java.util.Properties;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringbootRabbitProducer extends Base {

    /** Queue name for default exchange */
    static final String QUEUE = "standalone-queue";

    /** Exchange name to be created */
    static final String EXCHANGE_NAME = "my-exchange";

    /** Queue name to be created */
    static final String EXCHANGE_QUEUE_NAME = "my-exchange-queue";

    /** Exchange-queue routing key */
    static final String EXCHANGE_QUEUE_ROUTING_KEY = "my-routing-key";

    @Autowired
    RabbitTemplate rabbitTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRabbitProducer.class, args);
    }

    /**
     * Create a queue without an exchange. If it exists, does nothing.
     */
    @Bean
    CommandLineRunner createQueue() {
        return args -> {
            RabbitAdmin ra = new RabbitAdmin(rabbitTemplate);
            Properties prop = ra.getQueueProperties(QUEUE); 
            
            if(prop == null) {
                ra.declareQueue(new Queue(QUEUE));
                info("Queue '{}' does NOT exist", QUEUE);

                ra.declareQueue(new Queue(QUEUE));
                info("Queue '{}' created", QUEUE);
            }
            else {
                info("Queue '{}' DOES exist", QUEUE);
            }
        };
    }

    /**
     * Create queue and the exchange. If exists, does nothing.
     */
    // FYI:
    // The code has an issue with binding the queue to the exchange. 
    // The exchange and queue is created but the binding is NOT binding.
    // The solution is to declare the exchange, queue and binding as beans for Spring - then it works.
    // @Bean
    // CommandLineRunner createExchangeQueue() {
    //     return args -> {
    //         RabbitAdmin ra = new RabbitAdmin(rabbitTemplate);

    //         DirectExchange exchange = new DirectExchange(EXCHANGE_NAME);
    //         ra.declareExchange(exchange);

    //         Properties prop = ra.getQueueProperties(EXCHANGE_QUEUE_NAME); 
    //         if(prop == null) {
    //             Queue queue = new Queue(EXCHANGE_QUEUE_NAME);
                
    //             ra.declareQueue(queue);
    //             info("Queue '{}' does NOT exist", EXCHANGE_QUEUE_NAME);

    //             BindingBuilder.bind(queue).to(exchange).with("crazy");
    //             info("2 Queue bound to exchange");
    //         }
    //         else {
    //             info("2 Queue '{}' DOES exist!", EXCHANGE_QUEUE_NAME);
    //             Queue queue = new Queue(EXCHANGE_QUEUE_NAME);
    //             BindingBuilder.bind(queue).to(exchange).with("crazy");
    //             info("2 Queue bound to exchange");
    //         }
    //     };
    // }

    //
    // Create the exchange and queue - then bind them togeter
    //
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }
    @Bean
    Queue queue() {
        return new Queue(EXCHANGE_QUEUE_NAME, false);
    }
    @Bean 
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(EXCHANGE_QUEUE_ROUTING_KEY);
    }


    /**
     * Send a message to the rabbit queue.
     * @param msg Message to send (must be JSON)
     * @return Empty body, OK status code if no errors
     */
    @PostMapping(path="/msg", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> sendMessage(@RequestBody String msg, @RequestParam(required = false) String exchange, 
        @RequestParam(required = false) String routingKey, @RequestParam(required = false) String queue) throws Exception {
        info("exchange = {}, routingKey = {}", exchange, routingKey);
        info("[x] received msg to send: {}", msg);
        if(exchange != null && routingKey != null) { 
            // User specified exchange and routingKey names
            info("[x] exchange and routingKey specified");
            rabbitTemplate.convertAndSend(exchange, routingKey, msg);
            info("[x] sent msg to rabbit 1 - exchange = '{}', routingKey = '{}' [OK]", exchange, routingKey);
        }
        else if(queue != null) { 
            // User specified only the queue name --> send to queue 
            info("[x] queue specified");
            rabbitTemplate.convertAndSend(queue, msg);
            info("[x] sent msg to rabbit 2 - queue = '{}' [OK]", queue);
        }
        else {
            info("[x] NO exchange and queue specified");
            rabbitTemplate.convertAndSend(QUEUE, msg);
            info("[x] sent msg to rabbit 3 - queue = '{}' [OK]", QUEUE);
        }
        
        return ResponseEntity.ok().build();
    }
}
