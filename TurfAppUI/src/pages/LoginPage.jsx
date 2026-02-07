import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import { useAuth } from "../context/AuthContext";
import "./LoginPage.css";

const LoginPage = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [showPassword, setShowPassword] = useState(false);

  const { login } = useAuth(); //
  const navigate = useNavigate();

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
        "http://localhost:8086/api/users/login",
        formData,
      );

      //Store auth in memory (AuthContext)
      login({
        email: formData.email,
        password: formData.password,
        role: response.data.role,
        name: response.data.name,
      });

      // Optional
      localStorage.setItem("isLoggedIn", "true");
      localStorage.setItem("role", response.data.role);
      localStorage.setItem("clientName", response.data.name);
      localStorage.setItem("email", formData.email);

      // Navigation
      if (response.data.role === "CLIENT") {
        navigate("/client/dashboard");
      } else if (response.data.role === "ADMIN") {
        navigate("/admin/dashboard");
      } else {
        navigate("/");
      }
    } catch (error) {
      console.error("Login failed:", error);
      alert("Login failed!");
    }
  };

  return (
    <>
      {/* <nav className="auth-navbar">
        <div className="logo">
          <span className="logo-badge">Logo</span>
          <span className="logo-text">App Name</span>
        </div>

        <div className="nav-links">
          <Link to="/">Home</Link>
          <Link to="/turfs">demo Link</Link>
          <Link to="/register">Register</Link>
        </div>
      </nav> */}

      <div className="auth-container">
        <div className="auth-card">
          <h2>Login</h2>

          <form onSubmit={handleSubmit}>
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />

            <label>Password</label>
            <div className="password-box">
              <input
                type={showPassword ? "text" : "password"}
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
              />
              <button
                type="button"
                onClick={() => setShowPassword((prev) => !prev)}
              >
                {showPassword ? "Hide" : "Show"}
              </button>
            </div>

            <button type="submit" className="auth-btn">
              Login
            </button>
          </form>

          <p className="auth-footer">
            Don’t have an account? <Link to="/register">Sign Up</Link>
          </p>
        </div>
      </div>
    </>
  );
};

export default LoginPage;
