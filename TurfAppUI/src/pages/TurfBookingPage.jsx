import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useAuth } from "../context/AuthContext";
import "../styles/TurfBookingPage.css";

const TurfBookingPage = () => {
  const navigate = useNavigate();
  const { auth } = useAuth();

  //for turf
  const [turf, setTurf] = useState(null);
  const [loadingTurf, setLoadingTurf] = useState(true);

  // for booking
  const [bookingType, setBookingType] = useState("HOURLY");

  const [bookingDate, setBookingDate] = useState("");
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");

  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  // for accessories
  const [accessories, setAccessories] = useState([]);
  const [loadingAccessories, setLoadingAccessories] = useState(true);

  // loading state for ui
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const today = new Date().toISOString().split("T")[0];

  // fetch turf
  useEffect(() => {
    if (!auth) return;

    const fetchTurf = async () => {
      try {
        const res = await axios.get("http://localhost:8086/api/turf/getturf", {
          auth: {
            username: auth.email,
            password: auth.password,
          },
        });
        setTurf(res.data);
      } catch {
        setMessage("Failed to load turf details");
      } finally {
        setLoadingTurf(false);
      }
    };

    fetchTurf();
  }, [auth]);

  // fetch accessories
  useEffect(() => {
    if (!auth) return;

    const fetchAccessories = async () => {
      try {
        const res = await axios.get(
          "http://localhost:8086/api/accessories/available",
          {
            auth: {
              username: auth.email,
              password: auth.password,
            },
          },
        );

        setAccessories(
          res.data.map((a) => ({
            ...a,
            selectedQuantity: 0,
          })),
        );
      } catch {
        setMessage("Failed to load accessories");
      } finally {
        setLoadingAccessories(false);
      }
    };

    fetchAccessories();
  }, [auth]);

  //submit
  const handleSubmit = async () => {
    setMessage("");

    if (bookingType === "HOURLY" && (!bookingDate || !startTime || !endTime)) {
      setMessage("Date and time are required");
      return;
    }

    if (bookingType === "FULL_DAY" && !bookingDate) {
      setMessage("Date is required");
      return;
    }

    if (bookingType === "MULTI_DAY" && (!startDate || !endDate)) {
      setMessage("Start and end date required");
      return;
    }

    setLoading(true);

    try {
      const payload = {
        turfId: turf.id,
        bookingType,
        accessories: accessories
          .filter((a) => a.selectedQuantity > 0)
          .map((a) => ({
            accessoryId: a.id,
            quantity: a.selectedQuantity,
          })),
      };

      if (bookingType === "HOURLY") {
        payload.bookingDate = bookingDate;
        payload.startTime = startTime;
        payload.endTime = endTime;
      }

      if (bookingType === "FULL_DAY") {
        payload.bookingDate = bookingDate;
      }

      if (bookingType === "MULTI_DAY") {
        payload.startDate = startDate;
        payload.endDate = endDate;
      }

      await axios.post("http://localhost:8086/api/bookings/book", payload, {
        auth: {
          username: auth.email,
          password: auth.password,
        },
      });

      setMessage("Turf booked successfully");
    } catch (err) {
      setMessage(err.response?.data || "Booking failed");
    } finally {
      setLoading(false);
    }
  };

  if (loadingTurf || loadingAccessories) {
    return <div className="container mt-5 text-center">Loading...</div>;
  }

  return (
    <div className="container mt-4" style={{ maxWidth: "800px" }}>
      {/* discount-info */}
      <div className="info-box discount-box">
        <h4>Discount Policy</h4>

        <ul>
          <li>
            Discount is calculated based on your <strong>wallet balance</strong>
          </li>
          <li>
            Discount Formula:
            <strong> totalAmount ÷ 1000</strong>
          </li>
          <li>
            Maximum discount cap: <strong>50% of total amount</strong>
          </li>
          <li>
            Higher wallet balance = <strong>higher discount</strong>
          </li>
        </ul>

        <p className="note">
          Discount is applied automatically during booking.
        </p>
      </div>

      {/* booking card */}
      <div className="card shadow">
        <div className="card-body">
          {/* header */}
          <div className="d-flex justify-content-between mb-3">
            <h4>Book Turf</h4>
            <button
              className="btn btn-outline-secondary btn-sm"
              onClick={() => navigate("/client/dashboard")}
            >
              ← Back to Dashboard
            </button>
          </div>

          {/* turf info */}
          {turf && (
            <div className="card mb-3 border-0 bg-light">
              <div className="card-body py-3">
                <div className="row align-items-center">
                  <div className="col-md-4">
                    <strong>{turf.turfName}</strong>
                  </div>
                  <div className="col-md-4">
                    <strong>{turf.location}</strong>
                  </div>
                  <div className="col-md-4 text-end">
                    <span className="badge bg-success">
                      ₹{turf.pricePerHour}/hour
                    </span>
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* booking type */}
          <label className="form-label">Booking Type</label>
          <select
            className="form-select mb-3"
            value={bookingType}
            onChange={(e) => setBookingType(e.target.value)}
          >
            <option value="HOURLY">Hourly</option>
            <option value="FULL_DAY">Full Day</option>
            <option value="MULTI_DAY">Multi Day</option>
          </select>

          {/* conditions */}
          {bookingType === "HOURLY" && (
            <>
              <label className="form-label">Date</label>
              <input
                type="date"
                className="form-control mb-3"
                min={today}
                value={bookingDate}
                onChange={(e) => setBookingDate(e.target.value)}
              />

              <div className="row">
                <div className="col">
                  <label className="form-label">Start Time</label>
                  <input
                    type="time"
                    className="form-control"
                    step="3600"
                    value={startTime}
                    onChange={(e) => setStartTime(e.target.value)}
                  />
                </div>

                <div className="col">
                  <label className="form-label">End Time</label>
                  <input
                    type="time"
                    className="form-control"
                    step="3600"
                    value={endTime}
                    onChange={(e) => setEndTime(e.target.value)}
                  />
                </div>
              </div>
            </>
          )}

          {bookingType === "FULL_DAY" && (
            <input
              type="date"
              className="form-control"
              min={today}
              value={bookingDate}
              onChange={(e) => setBookingDate(e.target.value)}
            />
          )}

          {bookingType === "MULTI_DAY" && (
            <div className="row">
              <div className="col">
                <input
                  type="date"
                  className="form-control"
                  min={today}
                  value={startDate}
                  onChange={(e) => setStartDate(e.target.value)}
                />
              </div>
              <div className="col">
                <input
                  type="date"
                  className="form-control"
                  min={startDate || today}
                  value={endDate}
                  onChange={(e) => setEndDate(e.target.value)}
                />
              </div>
            </div>
          )}

          <hr />

          {/* accessories */}
          <h6>Available Accessories</h6>
          {accessories.map((acc, i) => (
            <div key={acc.id} className="row align-items-center mb-2">
              <div className="col-md-4">
                <strong>{acc.accessoryName}</strong>
                <div className="text-muted">₹{acc.pricePerHour}/hour</div>
              </div>
              <div className="col-md-3">Available: {acc.quantity}</div>
              <div className="col-md-3">
                <input
                  type="number"
                  min="0"
                  max={acc.quantity}
                  className="form-control form-control-sm"
                  value={acc.selectedQuantity}
                  disabled={acc.quantity === 0}
                  onChange={(e) => {
                    const updated = [...accessories];
                    updated[i].selectedQuantity = Math.min(
                      Number(e.target.value),
                      acc.quantity,
                    );
                    setAccessories(updated);
                  }}
                />
              </div>
            </div>
          ))}

          <div className="text-end mt-3">
            <button
              className="btn btn-primary btn-sm px-4"
              onClick={handleSubmit}
              disabled={loading}
            >
              {loading ? "Booking..." : "Book Turf"}
            </button>
          </div>

          {message && (
            <div className="alert alert-info mt-3 text-center">{message}</div>
          )}
        </div>
      </div>
    </div>
  );
};

export default TurfBookingPage;
