package chiroito.sandbox.jfr;

import jdk.jfr.Recording;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class HelloWorldSelfRecording {

    static final Path JFR_DUMP_PATH = Paths.get(System.getProperty("user.dir") + File.separator + "self_dump.jfr");

    public static void main(String[] args) throws Exception {

        try (Recording selfRecording = new Recording()) {

            selfRecording.enable(HelloEvent.class);
            selfRecording.start();

            HelloEvent event = new HelloEvent();
            event.msg = "Hello World";
            event.begin();

            TimeUnit.MILLISECONDS.sleep(20);
            TimeUnit.MILLISECONDS.sleep(20);
            TimeUnit.MILLISECONDS.sleep(20);

            event.commit();

            selfRecording.stop();
            selfRecording.dump(JFR_DUMP_PATH);
        }
    }
}
