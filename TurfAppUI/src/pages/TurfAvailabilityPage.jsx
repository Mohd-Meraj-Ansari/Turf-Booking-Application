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

  useEffect(() => {
    initAvailability();
  }, []);

  const initAvailability = () => {
    const initial = DAYS.map((day) => ({
      dayOfWeek: day,
      available: false,
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
