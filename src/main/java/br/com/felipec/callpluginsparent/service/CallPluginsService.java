package br.com.felipec.callpluginsparent.service;

import br.com.felipec.callpluginsparent.model.Results;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
public class CallPluginsService {

  @Autowired
  AmqpTemplate template;

  @Autowired
  RestTemplate restTemplate;

  private Integer rabbitMqMessages = 0;
  private Integer restTemplateMessages = 0;
  private Integer restTemplateError = 0;

  public Results testPlugins(Integer messages) {
    var rabbitMqStart = System.currentTimeMillis();
    log.info("Call Plugin with RabbitMQ");
    callRabbitMqPlugin(messages);
    while (!rabbitMqMessages.equals(messages)) {
      try {
        wait(1);
      } catch (Exception e) {
      }
    }
    var rabbitMqFinish = System.currentTimeMillis();
    var rabbitMqTimeElapsed = rabbitMqFinish - rabbitMqStart;

    var restTemplateStart = System.currentTimeMillis();
    log.info("Call Plugin with Rest Template");
    callRestPlugin(messages);
    var restTemplateFinish = System.currentTimeMillis();
    var restTemplateTimeElapsed = restTemplateFinish - restTemplateStart;

    log.info("\nRABBIT MQ TIME: " + rabbitMqTimeElapsed + "\nREST TEMPLATE TIME: "
        + restTemplateTimeElapsed + "\nREST TEMPLATE ERRORS: " + restTemplateError);

    var results = new Results().numberOfCalls(messages)
        .restTemplateSuccessfulCalls(restTemplateMessages)
        .rabbitMqSuccessfulCalls(rabbitMqMessages);

    rabbitMqMessages = 0;
    restTemplateMessages = 0;
    restTemplateError = 0;

    return results;
  }

  private void callRestPlugin(Integer calls) {
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("messagesQuantity", String.valueOf(calls));
    var entity = new HttpEntity<>(headers);
    ResponseEntity<String> response;
    while (calls != 0) {
      try {
        response = restTemplate.exchange(
            "http://localhost:8081/rest-template/call",
            HttpMethod.GET,
            entity,
            String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
          restTemplateMessages += 1;
        }
      } catch (Exception exception) {
        log.info(exception.getMessage());
        restTemplateError += 1;
      }
      calls--;
    }
  }

  private void callRabbitMqPlugin(Integer mq) {
    while (mq != 0) {
      template.convertAndSend("CalledPlugin", "f");
      mq--;
    }
  }

  @RabbitListener(queues = "Caller")
  private void listen(String message) {
    rabbitMqMessages += 1;
  }
}
