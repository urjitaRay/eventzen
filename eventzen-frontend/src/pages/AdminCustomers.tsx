import { useEffect, useState } from "react";
import API from "../api/axios";
import { useAuth } from "../context/useAuth";

type Booking = {
  bookingId: number;
  eventName: string;
  eventDate: string;
};

type Customer = {
  id: number;
  name: string;
  email: string;
  phone: string;
  bookings: Booking[];
};

const AdminCustomers = () => {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const { role } = useAuth();

  useEffect(() => {
    API.get("/admin/customers")
      .then((res) => setCustomers(res.data))
      .catch(console.error);
  }, []);

  if (role !== "ADMIN") {
    return <div className="page">Access denied</div>;
  }

  return (
    <div className="page">
      <h1 className="title">Registered Customers</h1>

      <table className="table">
        <thead>
          <tr>
            <th>Customer</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Booked Events</th>
          </tr>
        </thead>

        <tbody>
          {customers.map((c) => (
            <tr key={c.id}>
              <td>{c.name}</td>
              <td>{c.email}</td>
              <td>{c.phone || "N/A"}</td>

              <td>
                {c.bookings.length > 0 ? (
                  c.bookings.map((b) => (
                    <span key={b.bookingId} className="tag">
                      {b.eventName} (
                      {new Date(b.eventDate).toLocaleDateString()})
                    </span>
                  ))
                ) : (
                  <span className="text-muted">No bookings</span>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminCustomers;