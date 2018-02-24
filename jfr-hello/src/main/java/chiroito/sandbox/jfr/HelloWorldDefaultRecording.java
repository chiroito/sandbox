package chiroito.sandbox.jfr;

import java.util.concurrent.TimeUnit;

public class HelloWorldDefaultRecording {

    /*
    * -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=filename=./defaut_dump.jfr
    * */
    public static void main(String[] args) throws Exception {

        HelloEvent event = new HelloEvent();
        event.msg = "Hello World";
        event.begin();

        TimeUnit.MILLISECONDS.sleep(20);
        TimeUnit.MILLISECONDS.sleep(20);
        TimeUnit.MILLISECONDS.sleep(20);

        event.commit();
    }
}
