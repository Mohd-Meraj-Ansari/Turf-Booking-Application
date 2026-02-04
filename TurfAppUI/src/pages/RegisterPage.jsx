import { useFormik } from "formik";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../styles/RegisterPage.css";
import { registerValidation } from "../validation/registerValidation";
import { useAuth } from "../context/AuthContext";

const RegisterPage = () => {
  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigate();
  const { logout } = useAuth();

  const registerUserObj = {
    name: "",
    email: "",
    password: "",
    role: "",
  };

  const { errors, values, touched, handleBlur, handleSubmit, handleChange } =
    useFormik({
      initialValues: registerUserObj,
      validationSchema: registerValidation,
      onSubmit: () => {
        saveData();
      },
    });

  async function saveData() {
    try {
      const response = await fetch("http://localhost:8086/api/users/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(values),
      });

      console.log(response);
      if (response.ok) {
        const data = await response.json();

        localStorage.setItem("username", values.name);
        localStorage.setItem("password", values.password);
        localStorage.setItem("role", data.role);

        alert("Registration successful! Login to continue");
        logout();
        navigate("/login");
      }
      const errorMessage = await response.text();
      alert(errorMessage);
    } catch (error) {
      console.error("Error submitting the form", error);
    }
  }

  return (
    <>
      {/* Navbar */}
      {/* <nav className="auth-navbar">
        <div className="logo">
          <span className="logo-badge">Logo</span>
          <span className="logo-text">App Name</span>
        </div>

        <div className="nav-links">
          <Link to="/">Home</Link>
          <Link to="/turfs">demo Link</Link>
          <Link to="/login">Login</Link>
        </div>
      </nav> */}

      {/* Register Card */}
      <div className="auth-container">
        <div className="auth-card">
          <h2>Sign Up</h2>

          <form onSubmit={handleSubmit}>
            {/* Name */}
            <label>Name</label>
            <input
              type="text"
              name="name"
              placeholder="User Name"
              value={values.name}
              onChange={handleChange}
              required
              onBlur={handleBlur}
            />
            {touched.name && errors.name && (
              <span className="error">{errors.name}</span>
            )}

            {/* Email */}
            <label>Email</label>
            <input
              type="email"
              name="email"
              placeholder="email"
              value={values.email}
              onChange={handleChange}
              onBlur={handleBlur}
            />
            {touched.email && errors.email && (
              <span className="error">{errors.email}</span>
            )}

            {/* Password */}
            <label>Password</label>
            <div className="password-box">
              <input
                type={showPassword ? "text" : "password"}
                name="password"
                placeholder="password"
                value={values.password}
                onChange={handleChange}
                onBlur={handleBlur}
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
              >
                {showPassword ? "Hide" : "Show"}
              </button>
            </div>
            {touched.password && errors.password && (
              <span className="error">{errors.password}</span>
            )}

            {/* Role */}
            <label>Role</label>
            <select
              name="role"
              value={values.role}
              onChange={handleChange}
              onBlur={handleBlur}
            >
              <option value="">-- Choose Role --</option>
              <option value="CLIENT">Client</option>
              <option value="ADMIN">Admin</option>
              {/* <option value="ADVERTISER">Advertiser</option> */}
            </select>
            {touched.role && errors.role && (
              <span className="error">{errors.role}</span>
            )}

            <button type="submit" className="auth-btn">
              Sign Up
            </button>
          </form>

          <p className="auth-footer">
            Already have an account? <Link to="/login">Login</Link>
          </p>
        </div>
      </div>
    </>
  );
};

export default RegisterPage;
