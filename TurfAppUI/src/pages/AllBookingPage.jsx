import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";
import "../styles/AllBookingPage.css";

const AllBookingsPage = () => {
  const { auth } = useAuth();

  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchBookings();
  }, []);

  async function fetchBookings() {
    try {
      const res = await axios.get(
        "http://localhost:8086/api/bookings/my-turf",
        {
          auth: {
            username: auth.email,
            password: auth.password,
          },
        },
      );
      // console.log(res.data);
      setBookings(res.data);
    } catch (err) {
      alert("Failed to load turf bookings");
    } finally {
      setLoading(false);
    }
  }

  if (loading) {
    return <p className="loading">Loading bookings...</p>;
  }

  return (
    <div className="admin-bookings-container">
      <div className="admin-bookings-card">
        <h2>Turf Bookings</h2>

        {bookings.length === 0 ? (
          <p className="empty">No bookings found</p>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr className="text-center">
                  <th>No</th>
                  <th>Client</th>
                  <th>Date(s)</th>
                  <th>Time</th>
                  <th>Total (₹)</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {bookings.map((b, index) => (
                  <tr key={b.bookingid}>
                    <td>{index + 1}</td>

                    <td>
                      <strong>{b.clientName}</strong>
                    </td>

                    <td>
                      {b.startDate === b.endDate ? (
                        b.startDate
                      ) : (
                        <>
                          {b.startDate}
                          <br />
                          <span className="muted">to</span>
                          <br />
                          {b.endDate}
                        </>
                      )}
                    </td>

                    <td>
                      {b.bookingType === "HOURLY" && (
                        <>
                          {b.startTime} – {b.endTime}
                        </>
                      )}

                      {b.bookingType === "FULL_DAY" && (
                        <span className="">Full Day</span>
                      )}

                      {b.bookingType === "MULTI_DAY" && (
                        <span className="">Multiple Days</span>
                      )}
                    </td>

                    <td>₹{b.advanceAmount.toFixed(2)}</td>

                    <td>
                      <span
                        className={`status-badge ${b.status.toLowerCase()}`}
                      >
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
    </div>
  );
};

export default AllBookingsPage;
