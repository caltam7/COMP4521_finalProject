package com.comp4521.cdar;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;

public class Event {
    public static final String PENDING = "Pending";
    public static final String APPROVED = "Approved";
    public static final String DENIED = "Denied";
    private LocalDateTime startTime, endTime;
    private Duration duration;
    private User participant, resourcesEntity, serviceProvider;
    private String status;

    public Event(LocalDateTime startTime, LocalDateTime endTime, User participantUid, User resourceUid, User serviceProviderUid, String status) {
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public User getParticipant() {
        return participant;
    }

    public User getResourcesEntity() {
        return resourcesEntity;
    }

    public User getServiceProvider() {
        return serviceProvider;
    }
}