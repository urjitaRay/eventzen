import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import API from "../api/axios";
import { useAuth } from "../context/useAuth";

const AddExpense = () => {
  const [expense, setExpense] = useState({
    amount: "",
    description: "",
    paymentStatus: "PENDING",
  });

  const [eventName, setEventName] = useState("");
  const [vendorName, setVendorName] = useState("");

  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { role } = useAuth();

  const eventId = searchParams.get("eventId");
  const vendorId = searchParams.get("vendorId");

  useEffect(() => {
    if (role !== "ADMIN") {
      alert("Access denied. Admin only.");
      navigate("/events");
      return;
    }

    if (!eventId || !vendorId) {
      alert("Event ID and Vendor ID are required.");
      navigate("/events");
      return;
    }

    API.get(`/api/events/${eventId}`)
      .then((res) => setEventName(res.data.eventName))
      .catch(console.error);

    API.get(`/api/vendors/event/${eventId}`)
      .then((res) => {
        const vendor = res.data.find(
          (v: { vendorId: number; vendorName: string }) =>
            v.vendorId === parseInt(vendorId)
        );
        if (vendor) {
          setVendorName(vendor.vendorName);
        }
      })
      .catch(console.error);
  }, [eventId, vendorId, role, navigate]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setExpense({ ...expense, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      await API.post(`/api/expenses/${eventId}/${vendorId}`, {
        amount: parseFloat(expense.amount),
        description: expense.description,
        paymentStatus: expense.paymentStatus,
      });

      alert("Expense added successfully");
      navigate(`/vendor-details?eventId=${eventId}`);
    } catch (err) {
      console.error("Error adding expense:", err);
      alert("Failed to add expense.");
    }
  };

  if (role !== "ADMIN") {
    return <div className="page">Access denied. Admin only.</div>;
  }

  return (
    <div className="page-small">
      <h2>Add Expense</h2>

      <div className="card">
        <p><strong>Event:</strong> {eventName}</p>
        <p><strong>Vendor:</strong> {vendorName}</p>
      </div>

      <form onSubmit={handleSubmit}>

        <div className="form-group">
          <label>Amount (₹):</label>
          <input
            type="number"
            name="amount"
            value={expense.amount}
            onChange={handleChange}
            required
            min="0"
            step="0.01"
            className="input"
          />
        </div>

        <div className="form-group">
          <label>Description:</label>
          <input
            type="text"
            name="description"
            value={expense.description}
            onChange={handleChange}
            required
            className="input"
          />
        </div>

        <div className="form-group">
          <label>Payment Status:</label>
          <select
            name="paymentStatus"
            value={expense.paymentStatus}
            onChange={handleChange}
            className="input"
          >
            <option value="PENDING">Pending</option>
            <option value="PAID">Paid</option>
          </select>
        </div>

        <div className="nav-auth">
          <button type="submit" className="btn btn-primary">
            Add Expense
          </button>

          <button
            type="button"
            onClick={() => navigate(`/vendor-details?eventId=${eventId}`)}
            className="btn btn-secondary"
          >
            Cancel
          </button>
        </div>

      </form>
    </div>
  );
};

export default AddExpense;