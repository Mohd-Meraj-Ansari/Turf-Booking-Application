import React from "react";
import "./AdminDashboard.css";
import { Link } from "react-router-dom";

const AdminDashboard = () => {
  const stats = [
    { label: "Total Users", value: 588 },
    { label: "Total Owners", value: 34 },
    { label: "Total Turfs", value: 32 },
    { label: "Total Bookings", value: 314 },
    { label: "Pending Requests", value: 0 },
    { label: "Rejected Requests", value: 31 },
    { label: "Total Revenue", value: "₹8,280" },
  ];

  return (
    <div className="admin-dashboard">
      <header className="dashboard-header">
        <h1>TS TurfSpot</h1>
        <button className="logout-btn">Logout</button>
      </header>

      <aside className="sidebar">
        <nav>
          <Link to="/admin" className="sidebar-link">Dashboard</Link>
          <Link to="/owner-requests" className="sidebar-link">Owner Requests</Link>
          <Link to="/new-requests" className="sidebar-link">New Requests</Link>
          <Link to="/rejected-requests" className="sidebar-link">Rejected Requests</Link>
          <Link to="/users" className="sidebar-link">Users</Link>
          <Link to="/owners" className="sidebar-link">Owners</Link>
          <Link to="/turfs" className="sidebar-link">Turfs</Link>
          <Link to="/transactions" className="sidebar-link">Transactions</Link>
        </nav>
      </aside>

      <main className="dashboard-main">
        <h2>Admin Dashboard</h2>
        <div className="stats-grid">
          {stats.map((stat, index) => (
            <div key={index} className="stat-card">
              <h3>{stat.label}</h3>
              <p>{stat.value}</p>
            </div>
          ))}
        </div>

        <section className="booking-history">
          <h3>Booking History</h3>
          <select>
            <option>Last 30 days</option>
            <option>Last 7 days</option>
            <option>All time</option>
          </select>
          {/* Booking table or chart can go here */}
        </section>
      </main>
    </div>
  );
};

export default AdminDashboard;
