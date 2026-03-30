import { useEffect, useState } from "react";
import API from "../api/axios";
import { type Event } from "../types/Events";
import { useAuth } from "../context/useAuth";

const MyEvents = () => {
  const [events, setEvents] = useState<Event[]>([]);
  const { role } = useAuth();

  useEffect(() => {
    API.get("/api/events/my").then((res) => setEvents(res.data));
  }, []);

  return (
    <div className="page-container">
      <h2 className="page-title">My Events</h2>

      {role === "CUSTOMER" && (
        <div className="info-box">
          <p>
            <strong>Future Scope:</strong> Customer can request event creation
          </p>
        </div>
      )}

      {events.length > 0 ? (
        <div className="list-container">
          {events.map((e) => (
            <div key={e.eventId} className="list-item">
              {e.eventName}
            </div>
          ))}
        </div>
      ) : (
        <div className="empty-state">No events found.</div>
      )}
    </div>
  );
};

export default MyEvents;