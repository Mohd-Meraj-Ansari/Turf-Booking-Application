import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";
import "../styles/TurfAvailabilityPage.css";

const DAYS = [
  "MONDAY",
  "TUESDAY",
  "WEDNESDAY",
  "THURSDAY",
  "FRIDAY",
  "SATURDAY",
  "SUNDAY",
];

const TurfAvailabilityPage = () => {
  const { auth } = useAuth();

  const [availability, setAvailability] = useState([]);
  const [loading, setLoading] = useState(true);
  const [globalOpenTime, setGlobalOpenTime] = useState("");
  const [globalCloseTime, setGlobalCloseTime] = useState("");

  useEffect(() => {
    initAvailability();
  }, []);

  const initAvailability = () => {
    const initial = DAYS.map((day) => ({
      dayOfWeek: day,
      available: true,
      openTime: "",
      closeTime: "",
    }));
    setAvailability(initial);
    setLoading(false);
  };

  const toggleAvailable = (index) => {
    const updated = [...availability];
    updated[index].available = !updated[index].available;

    if (!updated[index].available) {
      updated[index].openTime = "";
      updated[index].closeTime = "";
    }

    setAvailability(updated);
  };

  const handleTimeChange = (index, field, value) => {
    const updated = [...availability];
    updated[index][field] = value;
    setAvailability(updated);
  };

  // ✅ apply common time to all OPEN days
  const applyTimeToAllOpenDays = () => {
    const updated = availability.map((day) =>
      day.available
        ? {
            ...day,
            openTime: globalOpenTime,
            closeTime: globalCloseTime,
          }
        : day,
    );

    setAvailability(updated);
  };

  const handleSave = async () => {
    try {
      await axios.post(
        "http://localhost:8086/api/users/availability/add",
        availability,
        {
          auth: {
            username: auth.email,
            password: auth.password,
          },
        },
      );

      alert("Turf availability saved successfully ✅");
    } catch (err) {
      alert("Failed to save availability ❌");
    }
  };

  if (loading) return <p className="loading">Loading...</p>;

  return (
    <div className="availability-container">
      <div className="availability-card">
        <h2>Turf Availability</h2>

        {/* ✅ Common time section */}
        <div className="common-time horizontal">
          <input
            type="time"
            value={globalOpenTime}
            onChange={(e) => setGlobalOpenTime(e.target.value)}
          />

          <span className="to-text">to</span>

          <input
            type="time"
            value={globalCloseTime}
            onChange={(e) => setGlobalCloseTime(e.target.value)}
          />

          <button
            className="apply-btn"
            onClick={applyTimeToAllOpenDays}
            disabled={!globalOpenTime || !globalCloseTime}
          >
            Apply Time to All Days
          </button>
        </div>

        {availability.map((day, index) => (
          <div className="day-row" key={day.dayOfWeek}>
            <div className="day-name">{day.dayOfWeek}</div>

            <label className="switch">
              <input
                type="checkbox"
                checked={day.available}
                onChange={() => toggleAvailable(index)}
              />
              <span className="slider"></span>
            </label>

            {day.available ? (
              <div className="time-inputs">
                <input
                  type="time"
                  value={day.openTime}
                  onChange={(e) =>
                    handleTimeChange(index, "openTime", e.target.value)
                  }
                />
                <span>to</span>
                <input
                  type="time"
                  value={day.closeTime}
                  onChange={(e) =>
                    handleTimeChange(index, "closeTime", e.target.value)
                  }
                />
              </div>
            ) : (
              <span className="closed-text">Closed</span>
            )}
          </div>
        ))}

        <button className="save-btn" onClick={handleSave}>
          Save Availability
        </button>
      </div>
    </div>
  );
};

export default TurfAvailabilityPage;
