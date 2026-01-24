import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useAuth } from "../context/AuthContext";

const BookingHistoryPage = () => {
  const { auth } = useAuth();
  const navigate = useNavigate();

  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");

  //fetch booking history
  useEffect(() => {
    if (!auth) return;

    const fetchHistory = async () => {
      try {
        const res = await axios.get("http://localhost:8086/api/bookings/past", {
          auth: {
            username: auth.email,
            password: auth.password,
          },
        });
        setBookings(res.data);
      } catch (err) {
        setMessage("Failed to load booking history");
      } finally {
        setLoading(false);
      }
    };

    fetchHistory();
  }, [auth]);

  const handleCancel = async (bookingId) => {
    if (!window.confirm("Are you sure you want to cancel this booking?"))
      return;

    try {
      await axios.delete(
        `http://localhost:8086/api/bookings/cancel/${bookingId}`,
        {
          auth: {
            username: auth.email,
            password: auth.password,
          },
        },
      );

      // update UI
      setBookings((prev) =>
        prev.map((b) =>
          b.bookingid === bookingId ? { ...b, status: "CANCELLED" } : b,
        ),
      );
    } catch (err) {
      alert(err.response?.data || "Failed to cancel booking");
    }
  };

  //loading state
  if (loading) {
    return (
      <div className="container mt-5 text-center">
        <h5>Loading booking history...</h5>
      </div>
    );
  }

  return (
    <div className="container mt-4" style={{ maxWidth: "1000px" }}>
      <div className="alert alert-warning mt-3">
        <strong>Refund Policy:</strong>
        <ul className="mb-0 mt-2">
          <li>
            Cancellation <b>24 hours before</b> start time →{" "}
            <b>100% advance refund</b>
          </li>
          <li>
            Cancellation <b>12–24 hours before</b> start time →{" "}
            <b>50% advance refund</b>
          </li>
          <li>
            Cancellation <b>less than 12 hours</b> before start time →{" "}
            <b>40% advance refund</b>
          </li>
        </ul>
        <small className="text-muted">
          Refund amount will be credited back to your wallet.
        </small>
      </div>

      <div className="card shadow">
        <div className="card-body">
          {/* HEADER */}
          <div className="d-flex justify-content-between align-items-center mb-3">
            <h4>Booking History</h4>
            <button
              className="btn btn-outline-secondary btn-sm"
              onClick={() => navigate("/client/dashboard")}
            >
              ← Back to Dashboard
            </button>
          </div>

          {/* EMPTY STATE */}
          {bookings.length === 0 && (
            <div className="alert alert-info text-center">
              No booking history found
            </div>
          )}

          {/* TABLE */}
          {bookings.length > 0 && (
            <div className="table-responsive">
              <table className="table table-bordered table-hover align-middle">
                <thead className="table-light">
                  <tr>
                    <th>No</th>
                    <th>Turf</th>
                    <th>Date(s)</th>
                    <th>Time</th>
                    <th>Total (₹)</th>
                    <th>Advance (₹)</th>
                    <th>Status</th>
                    <th>Action</th>
                  </tr>
                </thead>

                <tbody>
                  {bookings.map((b, index) => (
                    <tr key={b.bookingid}>
                      <td>{index + 1}</td>

                      <td>
                        <strong>{b.turfName}</strong>
                      </td>

                      <td>
                        {b.startDate === b.endDate ? (
                          b.startDate
                        ) : (
                          <>
                            {b.startDate} <br /> <b>to</b> {b.endDate}
                          </>
                        )}
                      </td>

                      <td>
                        {b.startTime && b.endTime ? (
                          <>
                            {b.startTime} – {b.endTime}
                          </>
                        ) : (
                          <span className="text-muted">Full Day</span>
                        )}
                      </td>

                      <td>₹{b.totalAmount}</td>
                      <td>₹{b.advanceAmount}</td>

                      <td>
                        <span className="badge bg-success">{b.status}</span>
                      </td>

                      <td>
                        {b.status === "BOOKED" ? (
                          <button
                            className="btn btn-danger btn-sm"
                            onClick={() => handleCancel(b.bookingid)}
                          >
                            Cancel
                          </button>
                        ) : (
                          <span className="text-muted">—</span>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          {/* MESSAGE */}
          {message && (
            <div className="alert alert-danger mt-3 text-center">{message}</div>
          )}
        </div>
      </div>
    </div>
  );
};

export default BookingHistoryPage;
