import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../api/axios";

const AddVendor = () => {
  const [vendor, setVendor] = useState({
    vendorName: "",
    serviceType: "",
    phone: "",
    email: "",
    eventId: "",
  });

  const [eventName, setEventName] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    const eventIdFromUrl = new URLSearchParams(window.location.search).get("eventId");

    if (eventIdFromUrl) {
      setVendor((prev) => ({ ...prev, eventId: eventIdFromUrl }));
      fetchEventName(eventIdFromUrl);
    } else {
      alert("No event selected. Please add vendor from an event card.");
      navigate("/events");
    }
  }, [navigate]);

  const fetchEventName = async (eventId: string) => {
    try {
      setLoading(true);
      const res = await API.get(`/api/events/${eventId}`);
      setEventName(res.data.eventName);
    } catch (err) {
      console.error("Error fetching event:", err);
      setEventName("Unknown Event");
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setVendor({ ...vendor, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!vendor.eventId) {
      alert("Event ID is missing. Please add vendor from an event card.");
      return;
    }

    try {
      await API.post(`/api/vendors/${vendor.eventId}`, {
        vendorName: vendor.vendorName,
        serviceType: vendor.serviceType,
        phone: vendor.phone,
        email: vendor.email,
      });

      alert("Vendor added successfully");
      navigate("/events");
    } catch (err) {
      console.error("Error adding vendor:", err);
      alert("Failed to add vendor. Please try again.");
    }
  };

  return (
    <div className="page-small">
      <h2 className="title">Add Vendor to Event</h2>

      <form onSubmit={handleSubmit}>

        <div className="card">
          <p>
            Event:{" "}
            <strong>{loading ? "Loading..." : eventName || "Unknown Event"}</strong>
          </p>
        </div>

        <div className="form-group">
          <label>Vendor Name</label>
          <input
            type="text"
            name="vendorName"
            value={vendor.vendorName}
            onChange={handleChange}
            required
            className="input"
          />
        </div>

        <div className="form-group">
          <label>Service Type</label>
          <input
            type="text"
            name="serviceType"
            value={vendor.serviceType}
            onChange={handleChange}
            required
            className="input"
          />
        </div>

        <div className="form-group">
          <label>Phone</label>
          <input
            type="tel"
            name="phone"
            value={vendor.phone}
            onChange={handleChange}
            required
            className="input"
          />
        </div>

        <div className="form-group">
          <label>Email</label>
          <input
            type="email"
            name="email"
            value={vendor.email}
            onChange={handleChange}
            required
            className="input"
          />
        </div>

        <button type="submit" className="btn btn-primary">
          Add Vendor
        </button>

      </form>
    </div>
  );
};

export default AddVendor;