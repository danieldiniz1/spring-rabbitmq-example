package br.com.sh.apprabbitmqconfig.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Value("${rabbitmq.exampleOneexchange.exchange}")
    private String EXCHANGE_ONE_NAME;

    @Value("${rabbitMq.exampleTwoexchange.exchange}")
    private String EXCHANGE_TWO_NAME;

    @Value("${rabbitmq.exampleOneexchange.dlq.exchange}")
    private String EXCHANGE_ONE_NAME_DLQ;

    @Value("${rabbitMq.exampleTwoexchange.dlq.exchange}")
    private String EXCHANGE_TWO_NAME_DLQ;

    @Value("${rabbitmq.exampleOnequeue.queue}")
    private String QUEUE_ONE_NAME;

    @Value("${rabbitmq.exampleOnequeue.dlq.queue}")
    private String QUEUE_ONE_NAME_DLQ;

    @Value("${rabbitmq.exampleTwoqueue.queue}")
    private String QUEUE_TWO_NAME;

    @Value("${rabbitmq.exampleTwoqueue.dlq.queue}")
    private String QUEUE_TWO_NAME_DLQ;

    private final ConnectionFactory connectionFactory;

    public QueueConfig(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    //    configura o rabbit admin e listener
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationListener(RabbitAdmin rabbitAdmin) {
        return event -> {
            // Initialize RabbitAdmin after the application is ready
            rabbitAdmin.initialize();
        };
    }

    //    Congfigura o FanoutExchange
    @Bean
    public FanoutExchange fanoutOneExchange() {
        return ExchangeBuilder.fanoutExchange(EXCHANGE_ONE_NAME).build();
    }

    @Bean
    public FanoutExchange fanoutOneDLQExchange() {
        return ExchangeBuilder.fanoutExchange(EXCHANGE_ONE_NAME_DLQ).build();
    }

    @Bean
    public FanoutExchange fanoutTwoExchange() {
        return ExchangeBuilder.fanoutExchange(EXCHANGE_TWO_NAME).build();
    }

    @Bean
    public FanoutExchange fanoutTwoDLQExchange() {
        return ExchangeBuilder.fanoutExchange(EXCHANGE_TWO_NAME_DLQ).build();
    }

    //    Configura e cria as filas
    @Bean
    public Queue createQueueOne() {
        return QueueBuilder
                .durable(QUEUE_ONE_NAME)
                .deadLetterExchange(EXCHANGE_ONE_NAME_DLQ)
                .build();
    }

    @Bean
    public Queue createQueueOneDLQ() {
        return QueueBuilder
                .durable(QUEUE_ONE_NAME_DLQ)
                .build();
    }

    @Bean
    public Queue createQueueTwo() {
        return QueueBuilder
                .durable(QUEUE_TWO_NAME)
                .deadLetterExchange(EXCHANGE_TWO_NAME_DLQ)
                .build();
    }

    @Bean
    public Queue createQueueTwoDLQ() {
        return QueueBuilder
                .durable(QUEUE_TWO_NAME_DLQ)
                .build();
    }

    //    Congfigura o Binding
    @Bean
    public Binding createBindingOneQueue() {
        return BindingBuilder.bind(createQueueOne())
                .to(fanoutOneExchange());
    }

    @Bean
    public Binding createBindingOneDLQQueue() {
        return BindingBuilder.bind(createQueueOneDLQ())
                .to(fanoutOneDLQExchange());
    }

    @Bean
    public Binding createBindingTwoQueue() {
        return BindingBuilder.bind(createQueueTwo())
                .to(fanoutTwoExchange());
    }

    @Bean
    public Binding createBindingTwoDLQQueue() {
        return BindingBuilder.bind(createQueueTwoDLQ())
                .to(fanoutTwoDLQExchange());
    }


    //    configura o message converter
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //    configura o RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
