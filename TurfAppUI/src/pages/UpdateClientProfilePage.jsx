import React, { useState } from "react";
import { useAuth } from "../context/AuthContext";
import axios from "axios";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";

const UpdateProfile = () => {
  const [message, setMessage] = useState("");
  const basicAuth = localStorage.getItem("basicAuth");

  const { auth } = useAuth();
  console.log("AUTH CONTEXT:", auth);

  // Validation schema
  const validationSchema = Yup.object({
    name: Yup.string()
      .min(3, "Name must be at least 3 characters")
      .nullable(),
    password: Yup.string()
      .min(6, "Password must be at least 6 characters")
      .nullable(),
  });

  const handleSubmit = async (values, { setSubmitting, resetForm }) => {
  try {
    await axios.put(
      "http://localhost:8086/api/users/update-my-profile",
      {
        name: values.name,
        password: values.password,
      },
      {
        auth: {
          username: auth.email,
          password: auth.password,
        },
      }
    );

    // 🔥 IMPORTANT FIX
    if (values.password) {
      login({
        ...auth,
        password: values.password, // update auth password
      });
    }

    setMessage("Profile updated successfully ✅");
    resetForm();
  } catch (error) {
    setMessage("Failed to update profile ❌");
  } finally {
    setSubmitting(false);
  }
};

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-body">
              <h4 className="card-title text-center mb-4">
                Update Profile
              </h4>

              <Formik
                initialValues={{ name: "", password: "" }}
                validationSchema={validationSchema}
                onSubmit={handleSubmit}
              >
                {({ isSubmitting }) => (
                  <Form>
                    {/* Name */}
                    <div className="mb-3">
                      <label className="form-label">Name</label>
                      <Field
                        type="text"
                        name="name"
                        className="form-control"
                        placeholder="Enter new name"
                      />
                      <ErrorMessage
                        name="name"
                        component="div"
                        className="text-danger small"
                      />
                    </div>

                    {/* Password */}
                    <div className="mb-3">
                      <label className="form-label">New Password</label>
                      <Field
                        type="password"
                        name="password"
                        className="form-control"
                        placeholder="Enter new password"
                      />
                      <ErrorMessage
                        name="password"
                        component="div"
                        className="text-danger small"
                      />
                    </div>

                    <button
                      type="submit"
                      className="btn btn-primary w-100"
                      disabled={isSubmitting}
                    >
                      {isSubmitting ? "Updating..." : "Update Profile"}
                    </button>
                  </Form>
                )}
              </Formik>

              {message && (
                <div className="alert alert-info mt-3 text-center">
                  {message}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UpdateProfile ;
