package com.eventzen.repository;

import com.eventzen.model.Event;
import com.eventzen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByCreatedBy(User user); // ✅ ADD THIS
}