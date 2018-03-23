package chiroito.sandbox.kafka.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;

@SpringBootApplication
@EnableBinding(Sink.class)
public class StdoutApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(StdoutApplication.class);
		app.setWebEnvironment(false);
		app.run(args);
	}

	@StreamListener(Sink.INPUT)
	public void log(Message<String> message) {
		System.out.println("Received message : " + message);
	}
}
