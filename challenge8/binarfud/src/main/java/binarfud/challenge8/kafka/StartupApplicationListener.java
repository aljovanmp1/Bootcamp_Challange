package binarfud.challenge8.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class StartupApplicationListener implements
    ApplicationListener<ContextRefreshedEvent> {

  @Autowired
  KafkaTemplate kafkaTemplate;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    kafkaTemplate.send("startup.binarfud", "running");
  }

  @KafkaListener(topics = {"startup.binarfud", "startup.user"}, groupId = "server-binarfud")
  public void listenGroupFoo(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    System.out.println("msg: " + message + "topic: " + topic);
  }
}