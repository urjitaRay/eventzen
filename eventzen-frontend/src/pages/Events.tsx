import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../api/axios";
import { useAuth } from "../context/useAuth";
import { type Event } from "../types/Events";

const Events = () => {
  const [events, setEvents] = useState<Event[]>([]);
  const [bookingCounts, setBookingCounts] = useState<{ [key: number]: number }>({});
  const navigate = useNavigate();
  const { role } = useAuth();

  useEffect(() => {
    API.get("/api/events")
      .then((res) => setEvents(res.data))
      .catch(console.error);
  }, []);

  useEffect(() => {
    if (role === "ADMIN" && events.length > 0) {
      const fetchBookingCounts = async () => {
        const counts: { [key: number]: number } = {};

        for (const event of events) {
          try {
            const response = await API.get(`/api/bookings/count/${event.eventId}`);
            counts[event.eventId] = response.data;
          } catch {
            counts[event.eventId] = 0;
          }
        }

        setBookingCounts(counts);
      };

      fetchBookingCounts();
    }
  }, [role, events]);

  const handleVendorDetails = (eventId: number) => {
    navigate(`/vendor-details?eventId=${eventId}`);
  };

  const handleBooking = async (eventId: number) => {
    try {
      await API.post(`/api/bookings/${eventId}`);
      alert("Booking successful!");
      navigate("/bookings");
    } catch (err: any) {

  if (err.response?.status === 409) {
    alert("Event already booked");
  } 
  else {
    const msg =
      err.response?.data?.message ||
      err.response?.data?.error ||
      "Booking failed";

    alert(msg);
  }

}
  };

  const handleCancelEvent = async (eventId: number) => {
  const confirmCancel = window.confirm("Are you sure you want to cancel this event?");

  if (!confirmCancel) return;

  try {
    await API.delete(`/api/events/${eventId}`);

    alert("Event cancelled successfully");

    // remove event from UI
    setEvents(events.filter((e) => e.eventId !== eventId));

  } catch (err) {
    console.error("Error cancelling event:", err);
    alert("Failed to cancel event");
  }
};

  return (
    <div className="page">
      <h1 className="title">Events</h1>

      <div className="grid">
        {events.map((e) => {
            const isPastEvent = new Date(e.eventDate) < new Date();

        return (
          <div key={e.eventId} className="card">

            <div className="card-header">
              <h3 className="card-title">{e.eventName || "Unnamed Event"}</h3>
              <span className="tag">{e.eventType || "General"}</span>
            </div>

            <div className="card-content">
              <p><strong>Date:</strong> {e.eventDate ? new Date(e.eventDate).toLocaleDateString() : "TBD"}</p>
              <p><strong>Time:</strong> {e.eventTime || "TBD"}</p>
              <p><strong>Venue:</strong> {e.venue || "TBD"}</p>
              <p><strong>Budget:</strong> ₹{(e.budget || 0).toLocaleString()}</p>
            </div>

            <p className="text-muted">
              {e.description || "No description available."}
            </p>

            {role === "ADMIN" && (
              <div className="info-box">
                <strong>Confirmed Bookings:</strong>{" "}
                {bookingCounts[e.eventId] || 0}
              </div>
            )}

            <div className="card-actions">
  {role === "ADMIN" ? (
    <>
      <button
        onClick={() => handleVendorDetails(e.eventId)}
        className="btn btn-primary"
      >
        Vendor Details
      </button>

      <button
        onClick={() => handleCancelEvent(e.eventId)}
        className="btn btn-danger"
        disabled={isPastEvent}
      >
        {isPastEvent ? "Past Event" : "Cancel Event"}
      </button>
    </>
  ) : (
    <button
      onClick={() => handleBooking(e.eventId)}
      className="btn btn-success"
      disabled={isPastEvent}
    >
      {isPastEvent ? "Past Event" : "Book Event"}
    </button>
  )}
</div>
          </div>
        );
      })}
      </div>
    </div>
  );
};

export default Events;