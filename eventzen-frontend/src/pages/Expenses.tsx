import { useEffect, useState } from "react";
import API from "../api/axios";
import { type Expense } from "../types/Expense";

const Expenses = () => {
  const [expenses, setExpenses] = useState<Expense[]>([]);

  useEffect(() => {
    API.get("/api/expenses")
      .then((res) => setExpenses(res.data))
      .catch(console.error);
  }, []);

  return (
    <div className="page">
      <h1 className="title">Expenses</h1>

      {expenses.length > 0 ? (
        <table className="table">
          <thead>
            <tr>
              <th>Event</th>
              <th>Amount</th>
              <th>Description</th>
              <th>Status</th>
              <th>Date</th>
            </tr>
          </thead>

          <tbody>
            {expenses.map((expense) => (
              <tr key={expense.expenseId}>
                <td>{expense.event.eventName}</td>

                <td className="amount">
                  ₹{expense.amount.toLocaleString()}
                </td>

                <td>{expense.description || "-"}</td>

                <td>
                  <span
                    className={
                      expense.paymentStatus === "PAID"
                        ? "status-paid"
                        : "status-pending"
                    }
                  >
                    {expense.paymentStatus}
                  </span>
                </td>

                <td>
                  {new Date(expense.expenseDate).toLocaleDateString()}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <div className="empty-state">
          No expenses found.
        </div>
      )}
    </div>
  );
};

export default Expenses;