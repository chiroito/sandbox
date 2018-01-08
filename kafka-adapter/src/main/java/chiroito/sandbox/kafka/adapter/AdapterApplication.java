package chiroito.sandbox.kafka.adapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;

@SpringBootApplication
@EnableBinding(Source.class)
public class AdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdapterApplication.class, args);
    }

    @InboundChannelAdapter(value = "hello", poller = @Poller(fixedDelay = "5000", maxMessagesPerPoll = "1"))
    public String source() {
        return "Hello";
    }
}