import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const ProtectedRoute = ({ children, allowedRole }) => {
  const { auth } = useAuth();

  // 🔒 Not logged in (lost on refresh)
  if (!auth) {
    return <Navigate to="/login" replace />;
  }

  // 🔐 Role-based access (optional)
  if (allowedRole && auth.role !== allowedRole) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default ProtectedRoute;
