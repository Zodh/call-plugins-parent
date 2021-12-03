package br.com.felipec.callpluginsparent.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndpointMessageConfig {

  public static final String CALLED_PLUGIN_EXCHANGE = "CalledPlugin";
  public static final String CALLED_PLUGIN_QUEUE = "PluginMessagesManager";
  public static final String CALLER_PLUGIN_QUEUE = "Caller";
  public static final String CALLER_PLUGIN_EXCHANGE = "CallerMessagesManager";

  public static final String CALLED_ROUTING_KEY = "CalledRK";
  public static final String CALLER_ROUTING_KEY = "CallerRK";

  @Bean
  DirectExchange pluginExchange() {
    return new DirectExchange(CALLED_PLUGIN_EXCHANGE);
  }

  @Bean
  Queue queue() {
    return new Queue(CALLED_PLUGIN_QUEUE, true, false, false);
  }

  @Bean
  Binding binding() {
    return new Binding(pluginExchange().getName(), DestinationType.EXCHANGE, pluginExchange().getName(), CALLED_ROUTING_KEY, null);
  }

  // Caller

  @Bean
  DirectExchange callerExchange() {
    return new DirectExchange(CALLER_PLUGIN_EXCHANGE);
  }

  @Bean
  Queue callerQueue() {
    return new Queue(CALLER_PLUGIN_QUEUE, true, false, false);
  }

  @Bean
  Binding callerBinding() {
    return new Binding(callerExchange().getName(), DestinationType.EXCHANGE, callerExchange().getName(), CALLER_ROUTING_KEY, null);
  }

}
