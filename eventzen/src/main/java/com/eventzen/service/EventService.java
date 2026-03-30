package com.eventzen.service;

import com.eventzen.model.Event;
import com.eventzen.model.User;

import java.util.List;

public interface EventService {

    Event createEvent(Event event, User user);

    List<Event> getAllEvents();

    Event getEventById(Long id);

    Event updateEvent(Long id, Event updatedEvent, User user);

    void deleteEvent(Long id, User user);

    List<Event> getEventsByUser(User user);

    
}