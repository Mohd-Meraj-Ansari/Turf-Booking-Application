import { useNavigate } from "react-router-dom";
import DashboardLayout from "../layouts/DashboardLayout";
import DashboardStats from "../components/DashboardStats";
import BookingHistory from "../components/BookingHistory";

const ClientDashboard = () => {
  const navigate = useNavigate();
  const clientName = localStorage.getItem("clientName");

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <DashboardLayout>
      <div className="d-flex justify-content-between align-items-center mb-4 dashboard-wrapper">
        <span className="fw-bolder fs-4">Welcome, {clientName}</span>
      </div>

      <DashboardStats />
      <BookingHistory />
    </DashboardLayout>
  );
};

export default ClientDashboard;
