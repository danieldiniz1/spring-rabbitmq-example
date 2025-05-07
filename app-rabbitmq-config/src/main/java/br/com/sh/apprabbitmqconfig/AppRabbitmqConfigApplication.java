package br.com.sh.apprabbitmqconfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Objects;


@SpringBootApplication
public class AppRabbitmqConfigApplication {

    private static final Logger LOGGER = LogManager.getLogger(AppRabbitmqConfigApplication.class);
    @Value("${rabbitmq.exampleOneexchange.exchange}")
    private String EX_ONE_NAME;

    public static void main(String[] args) {
        SpringApplication.run(AppRabbitmqConfigApplication.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, RabbitTemplate template) {
        return args -> {
            try {
                LOGGER.info("****************************Iniciando envio de mensagem");
                simualteProcess(15000L);
                carro carro = new carro(2023, "fiat", "preto");
                template.convertAndSend(EX_ONE_NAME,"", carro);
                LOGGER.info("Carro enviado com sucesso!");
            } catch (Exception e) {
                LOGGER.info("++++++++++++++++++não foi possivel enviar mensagem: {}", e.getMessage());
            }
        };
    }

//    @RabbitListener(queues = "${rabbitmq.exampleOnequeue.queue}",containerFactory = "listenerUm")
//    public void listenOne(Object message) throws InterruptedException {
////        LOGGER.info(" mensagem: {}",message);
//        LOGGER.info("process queue one");
//        simualteProcess(5000L);
//    }
//
//    @RabbitListener(queues = "${rabbitmq.exampleTwoqueue.queue}",containerFactory = "listenerDois")
//    public void listenTwo(Object message) throws InterruptedException {
////        LOGGER.info(" mensagem: {}",message);
//        LOGGER.info("process queue two");
//        simualteProcess(5000L);
//    }

    private void simualteProcess(Long time) throws InterruptedException {
        Thread.sleep(time);
    }

    public record carro (int ano, String modelo, String cor) {

    }
}
