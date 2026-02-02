import { useNavigate } from "react-router-dom";
import DashboardStats from "../components/DashboardStats";
import DashboardLayout from "../layouts/DashboardLayout";
import TurfStatusPage from "./TurfStatusPage";

const ClientDashboard = () => {
  const navigate = useNavigate();
  const clientName = localStorage.getItem("clientName");

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <DashboardLayout>
      <div className="d-flex justify-content-between align-items-center mb-4 ">
        <span className="fw-bolder fs-4">Welcome, {clientName}</span>
      </div>

      <DashboardStats />
      <TurfStatusPage />
    </DashboardLayout>
  );
};

export default ClientDashboard;
