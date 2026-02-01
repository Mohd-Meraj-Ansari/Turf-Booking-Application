import { useNavigate } from "react-router-dom";
import DashboardStats from "../components/DashboardStats";
import DashboardLayout from "../layouts/DashboardLayout";
import AllBookingPage from "./AllBookingPage";

import "../styles/AdminDashboard.css";
import AdminDashboardStats from "./AdminDashboardStats";

const AdminDashboard = () => {
  const navigate = useNavigate();
  const clientName = localStorage.getItem("clientName");
  return (
    <>
      <DashboardLayout>
        <div className="d-flex justify-content-between align-items-center mb-4 ">
          <span className="fw-bolder fs-4">Welcome, {clientName}</span>
        </div>
      </DashboardLayout>
      <AdminDashboardStats />
      <AllBookingPage />
    </>
  );
};

export default AdminDashboard;
