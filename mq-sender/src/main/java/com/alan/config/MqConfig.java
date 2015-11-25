package com.alan.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2015-11-24.
 */
@Data
@Component
@ConfigurationProperties(locations = "classpath:mq_config.yaml", prefix = "mq")
public class MqConfig implements Serializable {

    @Autowired
    private ApplicationContext applicationContext;

    private List<AlanQueue> queues;

    private List<AlanExchange> exchanges;

    private List<AlanBind> binds;

    @Data
    public static class AlanQueue {

        private String name;

        private Boolean durable;
    }

    @Data
    public static class AlanExchange {
        private String name;
    }

    @Data
    public static class AlanBind {
        private String queue;

        private List<BindToExchange> exchanges;
    }

    @Data
    public static class BindToExchange{
        private String exchange;
    }

    @PostConstruct
    public void init(){
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();

        Map<String , Queue> queueMap = new HashMap<>();
        Queue registerQueue;
        for(AlanQueue queue : this.queues){
            registerQueue = new Queue(queue.getName() , queue.getDurable());

            queueMap.put(queue.getName() , registerQueue);
            beanFactory.registerSingleton(queue.getName() , registerQueue);
        }

        Map<String , TopicExchange> exchangeMap = new HashMap<>();
        TopicExchange registerExchange;
        for(AlanExchange exchange : this.exchanges){
            registerExchange = new TopicExchange(exchange.getName());

            exchangeMap.put(exchange.getName() , registerExchange);
            beanFactory.registerSingleton(exchange.getName() , registerExchange);
        }

        //绑定exchange与queue关系
        Binding binding;
        int id=0;
        for(AlanBind bind : binds){
            for(BindToExchange bindToExchange : bind.getExchanges()){
                binding = BindingBuilder.bind(queueMap.get(bind.getQueue())).to(exchangeMap.get(bindToExchange.getExchange())).with(bind.getQueue());

                beanFactory.registerSingleton("bind_exchange_queue"+(id++), binding);
            }
        }
    }
}
