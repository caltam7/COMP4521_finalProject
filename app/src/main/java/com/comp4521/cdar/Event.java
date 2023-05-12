package com.comp4521.cdar;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
    public static final String PENDING = "Pending";
    public static final String APPROVED = "Approved";
    public static final String DENIED = "Denied";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
    private LocalDateTime startTime, endTime;
    private Duration duration;
    private String eventTitle, participant, resourcesEntity, serviceProvider;
    private String status, eid;

    public Event(String eventTitle, LocalDateTime startTime, LocalDateTime endTime, String participantUid, String resourceUid, String serviceProviderUid, String status) {
        this.eventTitle = eventTitle;
        this.eid = UUID.randomUUID().toString();
        this.startTime = startTime;
        if(endTime != null)
            this.endTime = endTime;
        else this.endTime = startTime.plusHours(1);
        this.duration = Duration.between(startTime, endTime);
        this.participant = participantUid;
        this.resourcesEntity = resourceUid;
        this.serviceProvider = serviceProviderUid;
        this.status = status;
    }

    public String getEventTitle(){ return eventTitle;}
    public String getEid(){ return eid; }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public String getStartTime_str() {
        return startTime.format(formatter);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getParticipant() {
        return participant;
    }

    public String getResourcesEntity() {
        return resourcesEntity;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }
}