package br.com.sh.apprabbitmqconfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AppRabbitmqConfigApplication {

    private static final Logger LOGGER = LogManager.getLogger(AppRabbitmqConfigApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AppRabbitmqConfigApplication.class, args);
    }

    @RabbitListener(queues = "${rabbitmq.exampleOnequeue.queue}",containerFactory = "listenerUm")
    public void listen(String message) {
        LOGGER.info(" mensagem: {}",message);
    }



}
