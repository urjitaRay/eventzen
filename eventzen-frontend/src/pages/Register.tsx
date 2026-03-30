import { useState } from "react";
import API from "../api/axios";
import { useNavigate } from "react-router-dom";
import "../styles/eventzen.css";

const Register = () => {
  const [user, setUser] = useState({
    name: "",
    email: "",
    password: "",
    phone: "",
  });

  const navigate = useNavigate();

  const handleRegister = async () => {
    try {
      await API.post("/auth/register", user);
      alert("Registered successfully");
      navigate("/");
    } catch {
      alert("Error registering");
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h2 className="auth-title">Register</h2>

        <input
          placeholder="Name"
          onChange={(e) => setUser({ ...user, name: e.target.value })}
          className="auth-input"
        />

        <input
          placeholder="Email"
          type="email"
          onChange={(e) => setUser({ ...user, email: e.target.value })}
          className="auth-input"
        />

        <input
          placeholder="Password"
          type="password"
          onChange={(e) => setUser({ ...user, password: e.target.value })}
          className="auth-input"
        />

        <input
          placeholder="Phone"
          type="tel"
          onChange={(e) => setUser({ ...user, phone: e.target.value })}
          className="auth-input"
        />

        <button onClick={handleRegister} className="auth-button">
          Register
        </button>
      </div>
    </div>
  );
};

export default Register;