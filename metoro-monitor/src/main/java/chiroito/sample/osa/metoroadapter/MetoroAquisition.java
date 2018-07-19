package chiroito.sample.osa.metoroadapter;

import chiroito.sample.osa.metoroadapter.entity.MetroTrainLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableBinding(Source.class)
@EnableScheduling
public class MetoroAquisition {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MetoroAquisition.class);
        app.run(args);
    }

    private static String METRO_TRAIN_LOCATION_ENDPOINT = "https://api.tokyometroapp.jp/api/v2/datapoints?rdf:type=odpt:Train&acl:consumerKey=7805330470191b6717872845c88cc477cb5d731667a1f03b24922a597b817620";

    private static Proxy oracleProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("jp-proxy", 80));


    @Autowired
    private Source kafkaClient;

    @Scheduled(fixedDelay = 30_000)
    void send() {

        // グラフ化を綺麗にするために時刻を 30 秒刻みに整理する
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime normalizedDateTime = now.withSecond((now.getSecond() / 30) * 30).withNano(0);
        String dataCreatedTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(normalizedDateTime);

        // 東京メトロの API へ接続して電車の位置情報を取得する
        SimpleClientHttpRequestFactory clientFactory = new SimpleClientHttpRequestFactory();
//        clientFactory.setProxy(oracleProxy);
        RestTemplate metroTrainClient = new RestTemplate(clientFactory);
        MetroTrainLocation[] metroTrains = metroTrainClient.getForObject(METRO_TRAIN_LOCATION_ENDPOINT, MetroTrainLocation[].class);

        //
        for (MetroTrainLocation metroTrain : metroTrains) {

            metroTrain.date = dataCreatedTime;

            if (metroTrain.toStation == null) {
                metroTrain.toStation = metroTrain.fromStation;
            }
        }

        // CSV形式に変換して Kafka へデータを投入する
        for (MetroTrainLocation metroTrain : metroTrains) {

            String csvToOsa = trainToCSV(metroTrain);
            Message<String> message = MessageBuilder.withPayload(csvToOsa).build();
            kafkaClient.output().send(message);
        }
    }


    private static final String trainToCSV(MetroTrainLocation train) {
        return "," + train.railway + "," + train.trainNumber + "," + train.delay + "," + train.fromStation + "," + train.toStation + "," + train.direction + "," + train.terminal + "," + train.date;
    }

}
