import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";
import StatCard from "./StatCard";

const DashboardStats = () => {
  const { auth } = useAuth();
  const [stats, setStats] = useState(null);

  useEffect(() => {
    if (!auth) return;

    const fetchStats = async () => {
      try {
        const res = await axios.get(
          "http://localhost:8086/api/client/dashboard/stats",
          {
            auth: {
              username: auth.email,
              password: auth.password,
            },
          },
        );
        setStats(res.data);
      } catch (err) {
        console.error("Failed to load dashboard stats");
      }
    };

    fetchStats();
  }, [auth]);

  if (!stats) {
    return <p className="text-center">Loading stats...</p>;
  }

  return (
    <div className="row g-3">
      <div className="col-12 col-sm-6 col-lg-3">
        <StatCard
          title="My Bookings"
          value={stats.totalBookings}
          badge="📅"
          borderColor="#0d6efd"
        />
      </div>

      <div className="col-12 col-sm-6 col-lg-3">
        <StatCard
          title="Upcoming"
          value={stats.upcomingBookings}
          badge="⏰"
          borderColor="#ffc107"
        />
      </div>

      <div className="col-12 col-sm-6 col-lg-3">
        <StatCard
          title="Completed"
          value={stats.completedBookings}
          badge="✅"
          borderColor="#198754"
        />
      </div>

      <div className="col-12 col-sm-6 col-lg-3">
        <StatCard
          title="Cancelled"
          value={stats.cancelledBookings}
          badge="❌"
          borderColor="#dc3545"
        />
      </div>

      <div className="col-12 col-sm-6 col-lg-3">
        <StatCard
          title="Wallet Balance"
          value={`₹${stats.walletBalance.toFixed(2)}`}
          badge="💳"
          borderColor="#fd7e14"
        />
      </div>

      <div className="col-12 col-sm-6 col-lg-3">
        <StatCard
          title="Total Spent"
          value={`₹${stats.totalSpent.toFixed(2)}`}
          badge="💰"
          borderColor="#20c997"
        />
      </div>
    </div>
  );
};

export default DashboardStats;
