package chiroito.sandbox.kafka.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableBinding(Source.class)
@EnableScheduling
public class ScheduleApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ScheduleApplication.class);
        app.setWebEnvironment(false);
        app.run(args);
    }

    @Autowired
    private Source source;

    @Scheduled(fixedDelay = 5000)
    void send() {
        source.output().send(MessageBuilder.withPayload("Hello").build());
    }
}
