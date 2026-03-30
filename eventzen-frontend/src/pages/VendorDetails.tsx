import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import API from "../api/axios";
import { useAuth } from "../context/useAuth";
import "../styles/eventzen.css";

type Vendor = {
  vendorId: number;
  vendorName: string;
  serviceType: string;
  phone: string;
  email: string;
};

type Expense = {
  expenseId: number;
  amount: number;
  description: string;
  paymentStatus: string;
};

const VendorDetails = () => {
  const [vendors, setVendors] = useState<Vendor[]>([]);
  const [expenses, setExpenses] = useState<{ [vendorId: number]: Expense[] }>({});
  const [eventName, setEventName] = useState<string>("");
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { role } = useAuth();

  const eventId = searchParams.get("eventId");

  useEffect(() => {
    if (eventId) {
      API.get(`/api/events/${eventId}`)
        .then((res) => setEventName(res.data.eventName))
        .catch(console.error);

      API.get(`/api/vendors/event/${eventId}`)
        .then((res) => setVendors(res.data))
        .catch(console.error);
    }
  }, [eventId]);

  useEffect(() => {
    if (vendors.length > 0) {
      const fetchExpenses = async () => {
        const expensesData: { [vendorId: number]: Expense[] } = {};

        for (const vendor of vendors) {
          try {
            const response = await API.get(`/api/expenses/vendor/${vendor.vendorId}`);
            expensesData[vendor.vendorId] = response.data;
          } catch {
            expensesData[vendor.vendorId] = [];
          }
        }

        setExpenses(expensesData);
      };

      fetchExpenses();
    }
  }, [vendors]);

  const handleAddVendor = () => {
    navigate(`/add-vendor?eventId=${eventId}`);
  };

  const handleAddExpense = (vendorId: number) => {
    navigate(`/add-expense?eventId=${eventId}&vendorId=${vendorId}`);
  };

  if (role !== "ADMIN") {
    return <div className="vendor-denied">Access denied. Admin only.</div>;
  }

  return (
    <div className="vendor-page">
      <div className="vendor-header">
        <h1 className="vendor-title">Vendors for {eventName || "Event"}</h1>
        <button onClick={handleAddVendor} className="vendor-add-btn">
          Add Vendor
        </button>
      </div>

      {vendors.length === 0 ? (
        <div className="vendor-empty">
          No vendors assigned to this event yet.
        </div>
      ) : (
        <div className="vendor-grid">
          {vendors.map((vendor) => (
            <div key={vendor.vendorId} className="vendor-card">
              <div className="vendor-card-header">
                <div>
                  <div className="vendor-name">{vendor.vendorName}</div>
                  <div className="vendor-detail">
                    <strong>Service:</strong> {vendor.serviceType}
                  </div>
                  <div className="vendor-detail">
                    <strong>Phone:</strong> {vendor.phone}
                  </div>
                  <div className="vendor-detail">
                    <strong>Email:</strong> {vendor.email}
                  </div>
                </div>

                <button
                  onClick={() => handleAddExpense(vendor.vendorId)}
                  className="vendor-expense-btn"
                >
                  Add Expense
                </button>
              </div>

              <div className="expenses-section">
                <div className="expenses-title">Expenses</div>

                {expenses[vendor.vendorId]?.length === 0 ? (
                  <div className="expense-description">
                    No expenses recorded yet.
                  </div>
                ) : (
                  expenses[vendor.vendorId]?.map((expense) => (
                    <div key={expense.expenseId} className="expense-item">
                      <div className="expense-header">
                        <span className="expense-amount">
                          ₹{expense.amount.toLocaleString()}
                        </span>

                        <span
                          className={
                            expense.paymentStatus === "PAID"
                              ? "expense-status-paid"
                              : "expense-status-pending"
                          }
                        >
                          {expense.paymentStatus}
                        </span>
                      </div>

                      {expense.description && (
                        <div className="expense-description">
                          {expense.description}
                        </div>
                      )}
                    </div>
                  ))
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default VendorDetails;