import axios from "axios";
import { useFormik } from "formik";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import * as Yup from "yup";
import { useAuth } from "../context/AuthContext";
import "../styles/RegisterPage.css";

const AddTurfPage = () => {
  const { auth } = useAuth();
  const navigate = useNavigate();
  const [message, setMessage] = useState("");
  const [imagePreview, setImagePreview] = useState(null);

  //validation schema
  const validationSchema = Yup.object({
    turfName: Yup.string()
      .min(3, "Minimum 3 characters")
      .required("Turf name is required"),

    location: Yup.string()
      .min(3, "Minimum 3 characters")
      .required("Location is required"),

    pricePerHour: Yup.number()
      .positive("Must be positive")
      .required("Price per hour is required"),
  });

  //formik
  const {
    values,
    errors,
    touched,
    handleChange,
    handleBlur,
    handleSubmit,
    setFieldValue,
    isSubmitting,
  } = useFormik({
    initialValues: {
      turfName: "",
      location: "",
      pricePerHour: "",
    },
    validationSchema,
    onSubmit: addTurf,
  });

  async function addTurf() {
    try {
      await axios.post(
        "http://localhost:8086/api/users/add-turf",
        {
          turfName: values.turfName,
          location: values.location,
          pricePerHour: values.pricePerHour,
        },
        {
          auth: {
            username: auth.email,
            password: auth.password,
          },
        },
      );

      alert("Turf added successfully");
      navigate("/admin/dashboard");
    } catch (error) {
      console.error(error);
      alert("Failed to add turf");
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Add Turf</h2>

        <form onSubmit={handleSubmit}>
          {/* Turf Name */}
          <label>Turf Name</label>
          <input
            type="text"
            name="turfName"
            placeholder="Enter turf name"
            value={values.turfName}
            onChange={handleChange}
            onBlur={handleBlur}
          />
          {touched.turfName && errors.turfName && (
            <span className="error">{errors.turfName}</span>
          )}

          {/* Location */}
          <label>Location</label>
          <input
            type="text"
            name="location"
            placeholder="Enter location"
            value={values.location}
            onChange={handleChange}
            onBlur={handleBlur}
          />
          {touched.location && errors.location && (
            <span className="error">{errors.location}</span>
          )}

          {/* Price */}
          <label>Price per Hour (₹)</label>
          <input
            type="number"
            name="pricePerHour"
            placeholder="Enter price"
            value={values.pricePerHour}
            onChange={handleChange}
            onBlur={handleBlur}
          />
          {touched.pricePerHour && errors.pricePerHour && (
            <span className="error">{errors.pricePerHour}</span>
          )}

          <button
            type="submit"
            className="auth-btn mb-3"
            disabled={isSubmitting}
          >
            {isSubmitting ? "Adding..." : "Add Turf"}
          </button>

          <Link to="/admin/dashboard" className="back-btn text-center">
            Back to Dashboard
          </Link>
        </form>
      </div>
    </div>
  );
};

export default AddTurfPage;
