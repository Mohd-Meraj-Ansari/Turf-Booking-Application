import axios from "axios";
import { useFormik } from "formik";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "../styles/RegisterPage.css";
import { updateValidationSchema } from "../validation/updateValidation";

const UpdateProfile = () => {
  const { auth, logout } = useAuth();
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [message, setMessage] = useState("");

  const updateProfileObj = {
    name: "",
    password: "",
  };

  const {
    errors,
    values,
    touched,
    handleBlur,
    handleSubmit,
    handleChange,
    isSubmitting,
  } = useFormik({
    initialValues: updateProfileObj,
    validationSchema: updateValidationSchema,
    onSubmit: updateProfile,
  });

  async function updateProfile() {
    if (!values.name && !values.password) {
      setMessage("Please update at least one field");
      return;
    }

    try {
      await axios.put(
        "http://localhost:8086/api/users/update-my-profile",
        {
          name: values.name || null,
          password: values.password || null,
        },
        {
          auth: {
            username: auth.email,
            password: auth.password,
          },
        },
      );
      alert("Profile updated successfully. Please login again.");
      logout();
      navigate("/login");
    } catch (error) {
      setMessage("Failed to update profile");
    }
  }

  return (
    <>
      {/* Update Profile Card */}
      <div className="auth-container">
        <div className="auth-card">
          <h2>Update Profile</h2>

          <form onSubmit={handleSubmit}>
            {/* Name */}
            <label>Name</label>
            <input
              type="text"
              name="name"
              placeholder="New name"
              value={values.name}
              onChange={handleChange}
              onBlur={handleBlur}
            />
            {touched.name && errors.name && (
              <span className="error">{errors.name}</span>
            )}

            {/* Password */}
            <label>New Password</label>
            <div className="password-box">
              <input
                type={showPassword ? "text" : "password"}
                name="password"
                placeholder="New password"
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

            {message && <p className="auth-message">{message}</p>}

            <button
              type="submit"
              className="auth-btn mb-3"
              disabled={isSubmitting}
            >
              {isSubmitting ? "Updating..." : "Update Profile"}
            </button>
            <Link to="/client/dashboard" className="back-btn text-center">
              Back to Dashboard
            </Link>
          </form>
        </div>
      </div>
    </>
  );
};

export default UpdateProfile;
