package chiroito.sandbox.kafka.schedule;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;

@EnableBinding(Sink.class)
public class HelloListener {

    @StreamListener(Sink.INPUT)
    public void log(Message<String> message) {
        System.out.println("Received message : " + message);
    }
}
