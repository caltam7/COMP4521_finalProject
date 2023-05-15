package com.comp4521.cdar;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
    public static final String PENDING = "Pending";
    public static final String APPROVED = "Approved";
    public static final String DENIED = "Denied";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private LocalDate eventDate;
    private LocalTime startTime, endTime;
    private Duration duration;
    private String eventTitle;
    private String status, eid, resName;

    public Event(String eventTitle, String res, LocalDate date, LocalTime startTime, LocalTime endTime, String status) {
        this.eventTitle = eventTitle;
        this.resName = res;
        this.eventDate = date;
        this.eid = UUID.randomUUID().toString();
        this.startTime = startTime;
        if(endTime != null)
            this.endTime = endTime;
        else this.endTime = startTime.plusHours(1);
        this.duration = Duration.between(startTime, endTime);
        //this.participant = participantUid;
        //this.resourcesEntity = resourceUid;
        //this.serviceProvider = serviceProviderUid;
        this.status = status;
    }

    public String getEventTitle(){ return eventTitle;}
    public String getEid(){ return eid; }
    public LocalTime getStartTime() {
        return startTime;
    }
    public String getStartTime_str() {
        return startTime.format(formatter);
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    /*public String getParticipant() {
        return participant;
    }

    public String getResourcesEntity() {
        return resourcesEntity;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }*/
}