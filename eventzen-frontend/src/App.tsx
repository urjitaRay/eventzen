import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Events from "./pages/Events";
import MyEvents from "./pages/MyEvents";
import Bookings from "./pages/Bookings";
import Expenses from "./pages/Expenses";
import AddEvent from "./pages/AddEvent";
import AddVendor from "./pages/AddVendor";
import VendorDetails from "./pages/VendorDetails";
import AddExpense from "./pages/AddExpense";
import AdminCustomers from "./pages/AdminCustomers";
import AdminRoute from "./components/AdminRoute";
import "./styles/eventzen.css";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <BrowserRouter>
       <Navbar />

      <Routes>
  {/* Public Routes */}
         <Route path="/" element={<Login />} /> 
        <Route path="/register" element={<Register />} />

        {/* Protected Routes */}
        <Route
          path="/events"
          element={
            <ProtectedRoute>
              <Events />
            </ProtectedRoute>
          }
        />

        <Route
          path="/my-events"
          element={
            <ProtectedRoute>
              <MyEvents />
            </ProtectedRoute>
          }
        />

        <Route
          path="/bookings"
          element={
            <ProtectedRoute>
              <Bookings />
            </ProtectedRoute>
          }
        />

        <Route
          path="/expenses"
          element={
            <ProtectedRoute>
              <Expenses />
            </ProtectedRoute>
          }
        />

        <Route
          path="/add-event"
          element={
            <ProtectedRoute>
              <AddEvent />
            </ProtectedRoute>
          }
        />

        <Route
          path="/add-vendor"
          element={
            <ProtectedRoute>
              <AddVendor />
            </ProtectedRoute>
          }
        />

        <Route
          path="/vendor-details"
          element={
            <ProtectedRoute>
              <VendorDetails />
            </ProtectedRoute>
          }
        />

        <Route
          path="/add-expense"
          element={
            <ProtectedRoute>
              <AddExpense />
            </ProtectedRoute>
          }
        />

        <Route
  path="/customers"
  element={
    <AdminRoute>
      <AdminCustomers />
    </AdminRoute>
  }
/>
        
</Routes>
    </BrowserRouter>
  );
}

export default App;