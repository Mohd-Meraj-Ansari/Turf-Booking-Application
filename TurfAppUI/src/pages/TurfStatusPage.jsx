import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";
import "../styles/TurfStatusPage.css";

const TurfStatusPage = () => {
  const { auth } = useAuth();

  const [date, setDate] = useState(new Date().toISOString().split("T")[0]);
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (auth?.email && auth?.password) {
      fetchSchedule();
    }
  }, [auth, date]);

  const fetchSchedule = async () => {
    try {
      setLoading(true);
      const res = await axios.get(
        "http://localhost:8086/api/turf/allavailability",
        {
          params: { date },
          auth: {
            username: auth.email,
            password: auth.password,
          },
        },
      );
      setData(res.data);
    } catch (err) {
      alert("Failed to load turf schedule");
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <p className="loading">Loading...</p>;
  if (!data) return <p>No data available</p>;

  return (
    <div className="table-container">
      {/* Header */}
      <div className="header">
        <h2>{data.turfName} – Availability</h2>

        <div className="date-picker">
          <label>Date</label>
          <input
            type="date"
            value={date}
            onChange={(e) => setDate(e.target.value)}
            className="ms-2"
          />
        </div>
      </div>

      {/* Day Info */}
      <div className="day-info fs-5">
        <div>
          <span>
            Day : <strong>{data.dayOfWeek}</strong>
          </span>
          {"  "}
          <span>
            Date : <strong>{data.date}</strong>
          </span>
        </div>

        <div>
          Status:
          {data.open ? (
            <span className="badge open ms-2">OPEN</span>
          ) : (
            <span className="badge closed ms-2">CLOSED</span>
          )}
        </div>
        <div>
          {data.open && (
            <span className="time ms-3">
              Opentime : <strong>{data.openTime}</strong> Closetime :{" "}
              <strong>{data.closeTime}</strong>
            </span>
          )}
        </div>
      </div>

      {/* Table OR Closed Message */}
      {data.open ? (
        <table className="status-table text-center">
          <thead>
            <tr>
              <th>#</th>
              <th>Start Time</th>
              <th>End Time</th>
              <th>Status</th>
            </tr>
          </thead>

          <tbody>
            {data.slots.map((slot, index) => (
              <tr key={index}>
                <td>{index + 1}</td>
                <td>{slot.startTime}</td>
                <td>{slot.endTime}</td>
                <td>
                  <span
                    className={`status-pill ${
                      slot.status === "BOOKED" ? "booked" : "available"
                    }`}
                  >
                    {slot.status}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p className="closed-text">🚫 Turf is closed for this day</p>
      )}
    </div>
  );
};

export default TurfStatusPage;
