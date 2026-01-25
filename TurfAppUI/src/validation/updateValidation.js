import * as yup from "yup";

export const updateValidationSchema = yup.object({
  name: yup
    .string()
    .required("Username is required")
    .min(3, "Minimum 3 characters is required")
    .nullable()
    .notRequired(),

  password: yup
    .string()
    .required("Password is mandatory")
    .min(8, "Password must be at least 8 characters")
    .matches(/[A-Z]/, "Password must contain at least one uppercase letter")
    .matches(/[a-z]/, "Password must contain at least one lowercase letter")
    .matches(/[0-9]/, "Password must contain at least one number")
    .matches(
      /[@$!%*?&#]/,
      "Password must contain at least one special character",
    )
    .nullable()
    .notRequired(),
});
