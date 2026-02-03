import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";
import "../styles/OwnerBookingsPage.css";

const OwnerBookingsPage = () => {
  const { auth } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [filteredBookings, setFilteredBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [dateFilter, setDateFilter] = useState("ALL");

  useEffect(() => {
    fetchBookings();
  }, []);

  useEffect(() => {
    applyDateFilter();
  }, [dateFilter, bookings]);

  async function fetchBookings() {
    try {
      const res = await axios.get(
        "http://localhost:8086/api/bookings/owner/bookings",
        {
          auth: {
            username: auth.email,
            password: auth.password,
          },
        },
      );
      setBookings(res.data);
      setFilteredBookings(res.data);
    } catch (error) {
      alert("Failed to load bookings");
    } finally {
      setLoading(false);
    }
  }

  const applyDateFilter = () => {
    if (dateFilter === "ALL") {
      setFilteredBookings(bookings);
      return;
    }

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const tomorrow = new Date(today);
    tomorrow.setDate(today.getDate() + 1);

    const filtered = bookings.filter((b) => {
      const bookingDate = new Date(b.startDate);
      bookingDate.setHours(0, 0, 0, 0);

      if (dateFilter === "TODAY") {
        return bookingDate.getTime() === today.getTime();
      }

      if (dateFilter === "TOMORROW") {
        return bookingDate.getTime() === tomorrow.getTime();
      }

      return true;
    });

    setFilteredBookings(filtered);
  };

  if (loading) return <p className="loading">Loading bookings...</p>;

  return (
    <div className="bookings-page">
      <div className="page-header">
        <h2>All Turf Bookings</h2>

        {/* Date Filter */}
        <select
          className="date-filter"
          value={dateFilter}
          onChange={(e) => setDateFilter(e.target.value)}
        >
          <option value="ALL">All</option>
          <option value="TODAY">Today</option>
          <option value="TOMORROW">Tomorrow</option>
        </select>
      </div>

      {filteredBookings.length === 0 ? (
        <p className="empty-text">No bookings found</p>
      ) : (
        <div className="table-wrapper">
          <table className="bookings-table">
            <thead>
              <tr>
                <th>Sr. No</th>
                <th>Client</th>
                <th>Date</th>
                <th>Time</th>
                <th>Amount (₹)</th>
                <th>Status</th>
              </tr>
            </thead>

            <tbody>
              {filteredBookings.map((b, index) => (
                <tr key={b.bookingid}>
                  <td>{index + 1}</td>
                  <td>{b.clientName}</td>

                  <td>
                    {b.startDate}
                    {b.endDate && b.startDate !== b.endDate
                      ? ` → ${b.endDate}`
                      : ""}
                  </td>

                  <td>
                    {b.startTime && b.endTime
                      ? `${b.startTime} - ${b.endTime}`
                      : "Full Day"}
                  </td>

                  <td>{b.advanceAmount.toFixed(2)}</td>

                  <td>
                    <span className={`status-badge ${b.status.toLowerCase()}`}>
                      {b.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default OwnerBookingsPage;
