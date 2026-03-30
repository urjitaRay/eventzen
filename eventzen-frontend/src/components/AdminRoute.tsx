import type { ReactNode } from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../context/useAuth";

type Props = {
  children: ReactNode;
};

const AdminRoute = ({ children }: Props) => {
  const token = localStorage.getItem("token");
  const { role } = useAuth();

  if (!token) {
    return <Navigate to="/" />;
  }

  if (role !== "ADMIN") {
    return <Navigate to="/events" />;
  }

  return <>{children}</>;
};

export default AdminRoute;