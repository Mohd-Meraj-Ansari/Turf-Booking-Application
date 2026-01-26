import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";
import "../styles/AddAccessoriesPage.css";

const AddAccessoriesPage = () => {
  const { auth } = useAuth();

  const [turf, setTurf] = useState(null);
  const [accessories, setAccessories] = useState([]);
  const [loading, setLoading] = useState(true);

  //load existing turf and accessories
  useEffect(() => {
    fetchData();
  }, []);

  async function fetchData() {
    try {
      const turfRes = await axios.get(
        "http://localhost:8086/api/turf/getturf",
        {
          auth: { username: auth.email, password: auth.password },
        },
      );

      setTurf(turfRes.data);

      const accRes = await axios.get(
        `http://localhost:8086/api/accessories/available`,
        { auth: { username: auth.email, password: auth.password } },
      );

      if (accRes.data.length > 0) {
        setAccessories(accRes.data);
      } else {
        setAccessories([{ accessoryName: "", pricePerHour: "", quantity: "" }]);
      }
    } catch (err) {
      alert("No accessory is added");
    } finally {
      setLoading(false);
    }
  }

  const handleChange = (index, field, value) => {
    const updated = [...accessories];
    updated[index][field] = value;
    setAccessories(updated);
  };

  const addAccessory = () => {
    setAccessories([
      ...accessories,
      { accessoryName: "", pricePerHour: "", quantity: "" },
    ]);
  };

  const removeAccessory = (index) => {
    const updated = accessories.filter((_, i) => i !== index);
    setAccessories(updated);
  };

  async function handleSubmit() {
    const validAccessories = accessories.filter(
      (a) =>
        a.accessoryName?.trim() &&
        Number(a.pricePerHour) > 0 &&
        Number(a.quantity) > 0,
    );
    try {
      await axios.post(
        "http://localhost:8086/api/accessories/add-multiple",
        validAccessories,
        { auth: { username: auth.email, password: auth.password } },
      );

      alert("Accessories saved successfully");
    } catch {
      alert("Failed to save accessories");
    }
  }

  if (loading) return <p className="loading">Loading...</p>;

  return (
    <div className="accessories-container">
      <div className="accessories-card">
        <h2>Add / Update Accessories</h2>

        {/* Turf Info */}
        <div className="turf-info horizontal">
          <div>
            <span className="label fs-6">Turf Name</span>
            <p>{turf.turfName}</p>
          </div>
          <div>
            <span className="label fs-6">Location</span>
            <p>{turf.location}</p>
          </div>
          <div>
            <span className="label fs-6">Price / hr</span>
            <p>₹{turf.pricePerHour}</p>
          </div>
        </div>

        {/* Accessories */}
        {accessories.map((acc, index) => (
          <div className="accessory-card" key={index}>
            <div className="accessory-header">
              <h4>Accessory #{index + 1}</h4>
              {accessories.length > 1 && (
                <button
                  className="remove-btn"
                  onClick={() => removeAccessory(index)}
                >
                  ✕
                </button>
              )}
            </div>

            <div className="accessory-grid">
              <input
                placeholder="Accessory Name"
                value={acc.accessoryName || ""}
                onChange={(e) =>
                  handleChange(index, "accessoryName", e.target.value)
                }
              />

              <input
                type="number"
                placeholder="Price / hour (₹)"
                value={acc.pricePerHour}
                onChange={(e) =>
                  handleChange(index, "pricePerHour", e.target.value)
                }
              />

              <input
                type="number"
                placeholder="Max Quantity"
                value={acc.quantity}
                onChange={(e) =>
                  handleChange(index, "quantity", e.target.value)
                }
              />
            </div>
          </div>
        ))}

        <button className="add-accessory-btn" onClick={addAccessory}>
          + Add Another Accessory
        </button>

        <button className="save-btn" onClick={handleSubmit}>
          Save Accessories
        </button>
      </div>
    </div>
  );
};

export default AddAccessoriesPage;
