package binarfud.user.kafka;

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
    kafkaTemplate.send("startup.user", "running");
  }

  @KafkaListener(topics = { "startup.binarfud", "startup.user" }, groupId = "server")
  public void listenGroupFoo(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    
    if (topic.equals("startup.binarfud")) {
      if (message.equals("running"))
        kafkaTemplate.send("startup.user", "running");
    }
    System.out.println("msg: " + message + "topic: " + topic);
  }
}