import StatCard from "./StatCard";

const DashboardStats = () => {
  return (
    <div className="row g-3">
      <div className="col-12 col-sm-6 col-lg-3">
        <StatCard
          title="My Bookings"
          value="12"
          badge="📅"
          borderColor="#0d6efd"
        />
      </div>

      <div className="col-12 col-sm-6 col-lg-3">
        <StatCard title="Upcoming" value="3" badge="⏰" borderColor="#ffc107" />
      </div>

      <div className="col-12 col-sm-6 col-lg-3">
        <StatCard
          title="Completed"
          value="8"
          badge="✅"
          borderColor="#198754"
        />
      </div>

      <div className="col-12 col-sm-6 col-lg-3">
        <StatCard
          title="Cancelled"
          value="1"
          badge="❌"
          borderColor="#dc3545"
        />
      </div>

      <div className="col-12 col-sm-6 col-lg-3">
        <StatCard
          title="Wallet Balance"
          value="₹1,200"
          badge="💳"
          borderColor="#fd7e14"
        />
      </div>

      <div className="col-12 col-sm-6 col-lg-3">
        <StatCard
          title="Total Spent"
          value="₹9,880"
          badge="💰"
          borderColor="#20c997"
        />
      </div>
    </div>
  );
};

export default DashboardStats;
