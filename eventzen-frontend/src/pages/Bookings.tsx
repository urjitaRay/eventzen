import { useEffect, useState } from "react";
import API from "../api/axios";
import { type Booking } from "../types/Booking";

const Bookings = () => {
  const [bookings, setBookings] = useState<Booking[]>([]);

  useEffect(() => {
    API.get("/api/bookings/my")
      .then((res) => setBookings(res.data))
      .catch(console.error);
  }, []);

  const handleCancelBooking = async (bookingId: number) => {
    if (window.confirm("Are you sure you want to cancel this booking?")) {
      try {
        await API.delete(`/api/bookings/${bookingId}`);

        setBookings(bookings.filter((b) => b.bookingId !== bookingId));

        alert("Booking cancelled successfully!");
      } catch (err) {
        console.error("Error cancelling booking:", err);
        alert("Failed to cancel booking. Please try again.");
      }
    }
  };

  const activeBookings = bookings.filter(
    (b) => b.rsvpStatus !== "CANCELLED"
  );

  return (
    <div className="page">
      <h2 className="title">My Bookings</h2>

      {activeBookings.length === 0 ? (
        <p className="text-muted">You have no active bookings.</p>
      ) : (
        <div className="grid">
          {activeBookings.map((b) => (
            <div key={b.bookingId} className="card">
              <p>
                <strong>Event:</strong> {b.event.eventName}
              </p>

              <p>
                <strong>Status:</strong>{" "}
                <span className="tag">{b.rsvpStatus}</span>
              </p>

              <p>
                <strong>Date:</strong> {b.bookingDate}
              </p>

              <button
                onClick={() => handleCancelBooking(b.bookingId)}
                className="btn btn-danger"
              >
                Cancel Booking
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Bookings;