package com.comp4521.cdar;
import java.util.ArrayList;
import java.util.List;

public class Calendar {
    private String title;
    private List<User> clients;
    private List<User> resourcesEntities;
    private List<User> serviceProviders;
    private List<List<Event>> calendar;

    public Calendar(String title, List<User> clients, List<User> resourcesAdmins, List<User> serviceProviders, List<List<Event>> calendar) {
        this.title = title;
        this.clients = clients;
        this.resourcesEntities = resourcesAdmins;
        this.serviceProviders = serviceProviders;
        this.calendar = calendar;
    }

    public String getTitle() {
        return title;
    }

    public List<User> getClients() {
        return clients;
    }

    public List<User> getResourcesEntities() {
        return resourcesEntities;
    }

    public List<User> getServiceProviders() {
        return serviceProviders;
    }

    public List<List<Event>> getCalendar() {
        return calendar;
    }

    public void setCalendar(List<List<Event>> calendar) {
        this.calendar = calendar;
    }
}