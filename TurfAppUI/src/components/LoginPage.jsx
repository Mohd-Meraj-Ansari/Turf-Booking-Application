import { useState } from "react";
import "./LoginPage.css";
import axios from "axios";

const LoginPage = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [showPassword, setShowPassword] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        "http://localhost:8086/api/users/login", // TurfBooking backend login API
        formData
      );

      console.log("Backend Response:", response.data);
      alert("Login successful!");

      // Example: store token if backend sends one
      // localStorage.setItem("token", response.data.token);

      // Redirect to dashboard/home page
      // window.location.href = "/dashboard";
    } catch (error) {
      console.error("Error:", error);
      alert("Login failed!");
    }
  };

  return (
    <div className="login-container d-flex justify-content-center align-items-center">
      <div className="card p-4 shadow" style={{ width: "25rem" }}>
        <h3 className="text-center mb-3">Turf Booking Login</h3>

        <form onSubmit={handleSubmit}>
          {/* Email */}
          <div className="mb-3">
            <label className="form-label">Email Address</label>
            <input
              type="email"
              className="form-control"
              placeholder="Enter email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          {/* Password */}
          <div className="mb-3">
            <label className="form-label">Password</label>
            <div className="input-group">
              <input
                type={showPassword ? "text" : "password"}
                className="form-control"
                placeholder="Enter password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
              />
              <button
                type="button"
                className="btn btn-outline-secondary"
                onClick={() => setShowPassword((prev) => !prev)}
              >
                {showPassword ? "Hide" : "Show"}
              </button>
            </div>
          </div>

          {/* Submit */}
          <button type="submit" className="btn btn-primary w-100">
            Login
          </button>
        </form>

        {/* Link to Register */}
        <p className="text-center mt-3">
          Don’t have an account? <a href="/register">Register here</a>
        </p>
      </div>
    </div>
  );
};

export default LoginPage;
