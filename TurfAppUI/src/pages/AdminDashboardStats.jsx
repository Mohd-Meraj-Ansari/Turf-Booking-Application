import axios from "axios";
import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import "../styles/AdminDashboardStats.css";

const StatCard = ({ title, value, icon, bg }) => {
  return (
    <div className="stat-card">
      <div className="stat-left">
        <p className="stat-title">{title}</p>
        <h2 className="stat-value">{value}</h2>
      </div>

      <div className={`stat-icon ${bg}`}>{icon}</div>
    </div>
  );
};

const AdminDashboardStats = () => {
  const { auth } = useAuth();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (auth?.email && auth?.password) {
      fetchDashboardStats();
    }
  }, [auth]);

  const fetchDashboardStats = async () => {
    try {
      const res = await axios.get("http://localhost:8086/api/admin/dashboard", {
        auth: {
          username: auth.email,
          password: auth.password,
        },
      });
      console.log(res.data);
      setStats(res.data);
    } catch (err) {
      alert("Failed to load dashboard data");
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <p className="loading">Loading dashboard...</p>;
  if (!stats) return <p>No data available</p>;

  return (
    <div className="stats-grid">
      <StatCard
        title="Today’s Bookings"
        value={`${stats.todaysBookings ?? 0}`}
        icon="🟢"
        bg="blue"
      />

      {/* <StatCard
        title="Today’s Earnings"
        value={`₹${stats.todaysEarnings.toFixed(2) ?? 0}`}
        icon="💰"
        bg="green"
      /> */}

      <StatCard
        title="Upcoming Bookings"
        value={stats.upcomingBookings ?? 0}
        icon="📅"
        bg="yellow"
      />

      <StatCard
        title="Cancelled Bookings"
        value={stats.cancelledBookings ?? 0}
        icon="❌"
        bg="red"
      />

      <StatCard
        title="Total Bookings"
        value={stats.totalBookings ?? 0}
        icon="🧾"
        bg="mint"
      />

      <StatCard
        title="Total Clients"
        value={stats.totalClients ?? 0}
        icon="👥"
        bg="orange"
      />
    </div>
  );
};

export default AdminDashboardStats;
