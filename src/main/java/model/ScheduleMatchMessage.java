package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by ntcong on 4/4/2017.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleMatchMessage {

    private String vid;

    @JsonProperty("connection_name")
    private String connectionName;

    @JsonProperty("departure_station_name")
    private String departureStationName;

    @JsonProperty("arrival_station_name")
    private String arrivalStationName;

    @JsonProperty("start_timestamp")
    private long startTime;

    @JsonProperty("end_timestamp")
    private long endTime;

}
