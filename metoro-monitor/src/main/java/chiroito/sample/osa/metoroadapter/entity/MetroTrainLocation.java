package chiroito.sample.osa.metoroadapter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @see
 * https://developer.tokyometroapp.jp/documents/railway#%E5%88%97%E8%BB%8A%E3%83%AD%E3%82%B1%E3%83%BC%E3%82%B7%E3%83%A7%E3%83%B3%E6%83%85%E5%A0%B1-odpt:Train
 */
public class MetroTrainLocation {

    /**
     * 鉄道路線
     */
    @JsonProperty("odpt:railway")
    public String railway;

    /**
     * 列車番号
     */
    @JsonProperty("odpt:trainNumber")
    public String trainNumber;

    /**
     * 遅延時間（秒）
     */
    @JsonProperty("odpt:delay")
    public int delay;

    /**
     * 列車が出発した駅
     */
    @JsonProperty("odpt:fromStation")
    public String fromStation;

    /**
     * 列車が向かっている駅
     */
    @JsonProperty("odpt:toStation")
    public String toStation;

    /**
     * 方面（渋谷方面行きodpt.RailDirection:TokyoMetro.Shibuyaなど。）
     */
    @JsonProperty("odpt:railDirection")
    public String direction;

    @JsonProperty("odpt:terminalStation")
    public String terminal;

    @JsonProperty("dc:date")
    public String date;

}
