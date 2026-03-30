import { useState } from "react";
import { AuthContext } from "./AuthContext";

// Helper function to decode JWT token
const decodeToken = (token: string) => {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload;
  } catch (_error) {
    return null;
  }
};

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [token, setToken] = useState<string | null>(
    localStorage.getItem("token")
  );

  const [role, setRole] = useState<string | null>(() => {
    const savedToken = localStorage.getItem("token");
    if (savedToken) {
      const decoded = decodeToken(savedToken);
      return decoded?.role || null;
    }
    return null;
  });

  const login = (token: string) => {
    localStorage.setItem("token", token);
    setToken(token);
    const decoded = decodeToken(token);
    setRole(decoded?.role || null);
  };

  const logout = () => {
    localStorage.removeItem("token");
    setToken(null);
    setRole(null);
  };

  return (
    <AuthContext.Provider value={{ token, role, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};