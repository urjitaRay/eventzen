package com.eventzen.service;

import com.eventzen.model.Event;
import com.eventzen.model.User;
import com.eventzen.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event createEvent(Event event, User user) {

        if (event.getEventDate().isBefore(LocalDate.now())) {
        throw new RuntimeException("Event date cannot be in the past");
        }

        if (!user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Only ADMIN can create events");
        }

        event.setCreatedBy(user);
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Override
    public Event updateEvent(Long id, Event updatedEvent, User user) {

        Event event = getEventById(id);

        if (!user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Only ADMIN can update events");
        }

        if (updatedEvent.getEventDate().isBefore(LocalDate.now())) {
        throw new RuntimeException("Event date cannot be in the past");
        }

        event.setEventName(updatedEvent.getEventName());
        event.setEventType(updatedEvent.getEventType());
        event.setEventDate(updatedEvent.getEventDate());
        event.setEventTime(updatedEvent.getEventTime());
        event.setVenue(updatedEvent.getVenue());
        event.setDescription(updatedEvent.getDescription());
        event.setBudget(updatedEvent.getBudget());

        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long id, User user) {

        if (!user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Only ADMIN can delete events");
        }

        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found");
        }

        eventRepository.deleteById(id);
    }

    @Override
    public List<Event> getEventsByUser(User user) {
        return eventRepository.findByCreatedBy(user);
    }
}