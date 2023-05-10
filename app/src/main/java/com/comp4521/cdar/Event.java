package com.comp4521.cdar;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;

public class Event {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<User> participants;
    private List<User> resourcesEntities;
    private List<User> serviceProviders;

    public Event(LocalDateTime startTime, LocalDateTime endTime, List<User> participants, List<User> resources, List<User> serviceProviders) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.participants = participants;
        this.resourcesEntities = resources;
        this.serviceProviders = serviceProviders;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public List<User> getResourcesEntities() {
        return resourcesEntities;
    }

    public List<User> getServiceProviders() {
        return serviceProviders;
    }
}