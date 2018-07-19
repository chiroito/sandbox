package chiroito.sample.osa.metoroadapter;

import chiroito.sample.osa.metoroadapter.entity.MapTrainLocation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@SpringBootApplication
public class ServingTrainInfo {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ServingTrainInfo.class);
        app.run(args);
    }

    private final Map<String, List<MapTrainLocation>> trainsCache = new ConcurrentHashMap<>();

    /**
     * @param date
     * @return
     */
    @GetMapping(path = "train/{date}")
    public List<MapTrainLocation> load(@PathVariable(value = "date") String date) {

        List<MapTrainLocation> data = this.trainsCache.get(date);

        if (data == null) {
            return Collections.EMPTY_LIST;
        }
        return data;
    }


    /**
     * @param postedTrains
     */
    @PostMapping(path = "train")
    public void cache(@RequestBody List<MapTrainLocation> postedTrains) {

        // 送られてきたデータを時間毎に分類する
        Map<String, List<MapTrainLocation>> trainsByDates = new HashMap<>();

        for (MapTrainLocation postedTrain : postedTrains) {

            if (!trainsByDates.containsKey(postedTrain.date)) {
                // 新たな時間のデータの場合は新規
                List<MapTrainLocation> trainsByDate = new ArrayList<>();
                trainsByDates.put(postedTrain.date, trainsByDate);
            }

            List<MapTrainLocation> trainsByDate = trainsByDates.get(postedTrain.date);
            trainsByDate.add(postedTrain);
        }

        // 分類したデータを格納していく
        // 面倒臭いので全てヒープ内に保存する。
        // 面倒臭いので更新は少ないはずなので synchronized 。
        synchronized (this.trainsCache) {

            for (Map.Entry<String, List<MapTrainLocation>> trainsByDateEntry : trainsByDates.entrySet()) {

                // 一つの時間毎にデータを追加していく
                String date = trainsByDateEntry.getKey();
                if (this.trainsCache.containsKey(date)) {

                    // 履歴データがすでに格納されている場合は、分類したデータを追加する
                    List<MapTrainLocation> trainsByDate = this.trainsCache.get(date);
                    trainsByDate.addAll(trainsByDateEntry.getValue());

                } else {

                    // 履歴データが格納されていない場合は、分類したデータをそのまま格納する
                    this.trainsCache.put(date, trainsByDateEntry.getValue());
                }
            }
        }

    }

}
