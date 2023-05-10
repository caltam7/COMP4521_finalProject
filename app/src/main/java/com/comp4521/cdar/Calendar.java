package com.comp4521.cdar;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calendar {
    private String title;
    private List<User> clients;
    private List<User> resourcesAdmins;
    private List<User> serviceProviders;
    private Map<Integer, List<Event>> calendar;

    public Calendar(String title, List<User> clients, List<User> resourcesAdmins, List<User> serviceProviders, int year, int month) {
        this.title = title;
        this.clients = clients;
        this.resourcesAdmins = resourcesAdmins;
        this.serviceProviders = serviceProviders;
        this.calendar = new HashMap<>();

        int[] monthlyCalendar = getMonthlyCalendar(year, month);

        // Initialize the calendar map with empty lists for each day of the month
        for (int i = 0; i < monthlyCalendar.length; i++) {
            if (monthlyCalendar[i] != 0) {
                calendar.put(monthlyCalendar[i], new ArrayList<>());
            }
        }
    }

    public int[] getMonthlyCalendar(int year, int month) {
        int[] calendar = new int[35];
        LocalDate date = LocalDate.of(year, month, 1);
        int daysInMonth = date.lengthOfMonth();
        DayOfWeek firstDayOfWeek = date.getDayOfWeek();
        int firstDayOffset = firstDayOfWeek.getValue() - 1;

        // Calculate the first day of the week of the last day of the month
        LocalDate lastDayOfMonth = LocalDate.of(year, month, daysInMonth);
        DayOfWeek lastDayOfWeek = lastDayOfMonth.getDayOfWeek();
        int lastDayOffset = 6 - lastDayOfWeek.getValue() + firstDayOffset;

        // Adjust the number of cells in the calendar
        if (lastDayOffset >= 7) {
            calendar = new int[35 + 7];
        }

        // Populate the calendar array with the days of the month
        for (int i = 0; i < calendar.length; i++) {
            if (i < firstDayOffset || i >= daysInMonth + firstDayOffset) {
                calendar[i] = 0;
            } else {
                calendar[i] = i - firstDayOffset + 1;
            }
        }

        return calendar;
    }

    public void addEvent(Event event) {
        int day = event.getStartTime().getDayOfMonth();
        if (!calendar.containsKey(day)) {
            calendar.put(day, new ArrayList<>());
        }
        calendar.get(day).add(event);
    }

    // Getter and setter
}