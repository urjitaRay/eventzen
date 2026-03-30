import { useState } from "react";
import API from "../api/axios";
import { useNavigate } from "react-router-dom";

const AddEvent = () => {
  const [event, setEvent] = useState({
    eventName: "",
    eventType: "",
    eventDate: "",
    eventTime: "",
    venue: "",
    description: "",
    budget: "",
  });

  const navigate = useNavigate();

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    setEvent({ ...event, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();

  const today = new Date();
  today.setHours(0, 0, 0, 0);

  const selectedDate = new Date(event.eventDate);

  if (selectedDate < today) {
    alert("Event date cannot be in the past.");
    return;
  }

  try {
    await API.post("/api/events", {
      ...event,
      budget: parseFloat(event.budget),
    });

    alert("Event created successfully!");
    navigate("/events");
  } catch (_error) {
    console.error("Error creating event:", _error);
    alert("Error creating event. Please try again.");
  }
};

  return (
    <div className="page-small">
      <h2>Add New Event</h2>

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Event Name:</label>
          <input
            type="text"
            name="eventName"
            value={event.eventName}
            onChange={handleChange}
            required
            className="input"
          />
        </div>

        <div className="form-group">
          <label>Event Type:</label>
          <select
            name="eventType"
            value={event.eventType}
            onChange={handleChange}
            required
            className="input"
          >
            <option value="">Select Event Type</option>
            <option value="CONFERENCE">Conference</option>
            <option value="WORKSHOP">Workshop</option>
            <option value="CONCERT">Concert</option>
            <option value="SPORTS">Sports</option>
            <option value="WEDDING">Wedding</option>
            <option value="BIRTHDAY">Birthday</option>
            <option value="CORPORATE">Corporate</option>
            <option value="OTHER">Other</option>
          </select>
        </div>

        <div className="form-group">
          <label>Event Date:</label>
          <input
            type="date"
            name="eventDate"
            value={event.eventDate}
            onChange={handleChange}
            required
             min={new Date().toISOString().split("T")[0]}
            className="input"
          />
        </div>

        <div className="form-group">
          <label>Event Time:</label>
          <input
            type="time"
            name="eventTime"
            value={event.eventTime}
            onChange={handleChange}
            required
            className="input"
          />
        </div>

        <div className="form-group">
          <label>Venue:</label>
          <input
            type="text"
            name="venue"
            value={event.venue}
            onChange={handleChange}
            required
            className="input"
          />
        </div>

        <div className="form-group">
          <label>Description:</label>
          <textarea
            name="description"
            value={event.description}
            onChange={handleChange}
            rows={4}
            required
            className="input"
          />
        </div>

        <div className="form-group">
          <label>Budget (₹):</label>
          <input
            type="number"
            name="budget"
            value={event.budget}
            onChange={handleChange}
            required
            min="0"
            step="0.01"
            className="input"
          />
        </div>

        <button type="submit" className="btn btn-primary">
          Create Event
        </button>
      </form>
    </div>
  );
};

export default AddEvent;