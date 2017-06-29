package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationMessage {

    private String dateTimeScheduled;

    private double longitude;

    private double latitude;

    private String vibeType;

}
