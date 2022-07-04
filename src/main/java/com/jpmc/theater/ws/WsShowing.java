package com.jpmc.theater.ws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@EqualsAndHashCode
@ToString
public class WsShowing {
    @JsonProperty("title")
    private String title;

    @JsonProperty("sequence_of_the_day")
    private int sequenceOfTheDay;

    @JsonProperty("show_start_time")
    private String showStartTime;

    @JsonProperty("running_time")
    private String runningTime;

    @JsonProperty("ticket_price")
    private String ticketPrice;

}
