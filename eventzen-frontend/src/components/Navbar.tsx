import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/useAuth";

const Navbar = () => {
  const { logout, role } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <nav className="navbar">
      <div className="nav-inner">

        <div className="nav-flex">
          <Link to="/events" className="nav-link nav-brand">
            🎉 EventZen
          </Link>
        </div>

        <div className="nav-links">
          {role && <Link to="/events" className="nav-link">Events</Link>}
          {role === "CUSTOMER" && <Link to="/my-events" className="nav-link">My Events</Link>}
          {role === "CUSTOMER" && <Link to="/bookings" className="nav-link">Bookings</Link>}
          {role === "ADMIN" && <Link to="/expenses" className="nav-link">Expenses</Link>}
          {role === "ADMIN" && <Link to="/add-event" className="nav-link">Add Event</Link>}
          {role === "ADMIN" && <Link to="/customers" className="nav-link">Customers</Link>}
        </div>

        <div className="nav-flex">
          {role ? (
            <>
              <span className="role-label">
                Role: <strong className="role-badge">{role}</strong>
              </span>

              <button onClick={handleLogout} className="logout-btn">
                Logout
              </button>
            </>
          ) : (
            <div className="nav-auth">
              <Link to="/" className="nav-link">Login</Link>
              <Link to="/register" className="nav-link">Register</Link>
            </div>
          )}
        </div>

      </div>
    </nav>
  );
};

export default Navbar;