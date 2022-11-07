package ru.skillbox.diplom.group25.microservice.post.service;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * KafkaSender
 *
 * @author alex90bar
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSender {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  @Value(value = "${acks-timeout-mseconds}")
  private Integer acksTimeoutMseconds;

  public void sendMessage(String topic, String key, Object msg) {
    try {
      kafkaTemplate.send(topic, key, msg).get(acksTimeoutMseconds, TimeUnit.MILLISECONDS);
    } catch (ExecutionException | InterruptedException | TimeoutException e) {
      log.error("Таймаут отправки сообщения - возможно, нет связи с Kafka! {} , stackTrace {}", e, Arrays.toString(e.getStackTrace()));
    }
    log.info("Отправка через Kafka в топик: {} сообщение: {}", topic, msg);
  }

}


