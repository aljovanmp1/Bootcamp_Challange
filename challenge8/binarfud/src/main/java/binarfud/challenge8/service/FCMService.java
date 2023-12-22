package binarfud.challenge8.service;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class FCMService {
    private Logger logger = LoggerFactory.getLogger(FCMService.class);

    public void sendMessageToTopic(String title, String msg, String topic)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageToTopic(title, msg, topic);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(message);
        String response = sendAndGetResponse(message);
        logger.info("Sent message to topic. topic: " + topic + ", " + response+ " msg "+jsonOutput);
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private Message getPreconfiguredMessageToTopic(String title, String msg, String topic) {
        Notification notification = Notification.builder()
                                        .setTitle(title)
                                        .setBody(msg)
                                        .build();
        return Message.builder()
                .setNotification(notification)
                .setTopic(topic)
                .build();
    }
}
