package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationPointMessage {

    private String vid;

    private double latitude;

    private double longitude;

    private long datetimeCreated;

}
