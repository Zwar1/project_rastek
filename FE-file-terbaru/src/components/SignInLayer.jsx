import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import api from "../apiService.js";

import { Icon } from "@iconify/react/dist/iconify.js";
import { Link } from "react-router-dom";

const SignInLayer = () => {
  const navigate = useNavigate();

  const [isUsername, setisUsername] = useState("");
  const [isPassword, setisPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState(""); // Error state

  const handleSubmitGeneric = async (
    endpoint,
    method,
    bodyData,
    successMessage
  ) => {
    try {
      const response = await api({
        method: method,
        url: `/${endpoint}`,
        data: bodyData,
      });

      console.log(successMessage, response.data);

      if (endpoint === "auth/login") {
        const token = response.data?.token || response.data?.data?.token;

        if (token) {
          localStorage.setItem("authToken", token);
          console.log("Token: ", token);
          navigate("/dashboard"); // Redirect setelah login berhasil
        } else {
          console.warn("Token Tidak Ditemukan");
        }

        setisUsername("");
        setisPassword("");
        setErrorMessage(""); // Reset error message
      }
    } catch (error) {
      const errorMessage = error.response
        ? error.response.data.error["Error Description"] ||
          "Invalid username or password"
        : "Invalid username or password";
      setErrorMessage(errorMessage); // Set pesan error
    }
  };

  const handleSubmitLogin = (e) => {
    e.preventDefault();
    handleSubmitGeneric(
      "auth/login",
      "post",
      { username: isUsername, password: isPassword },
      "Berhasil login!"
    );
  };

  return (
    <div className="sign-in d-flex justify-content-center align-items-center min-vh-10">
      <section className="d-flex justify-content-center">
        <div className="signin-card max-w-464-px mx-auto w-100 p-5 rounded-3">
          <h4 className="mb-12 text-center">Sign In to your Account</h4>
          <p className="mb-32 text-secondary-light text-lg text-center">
            Welcome back! please enter your detail
          </p>
          <form onSubmit={handleSubmitLogin}>
            {errorMessage && (
              <div className="alert alert-danger mb-16">{errorMessage}</div>
            )}
            <div className="icon-field mb-16">
              <span className="icon top-50 translate-middle-y">
                <Icon icon="mage:email" />
              </span>
              <input
                type="text"
                value={isUsername}
                onChange={(e) => setisUsername(e.target.value)}
                className="form-control h-56-px bg-neutral-50 radius-12"
                placeholder="Username"
              />
            </div>
            <div className="position-relative mb-20">
              <div className="icon-field">
                <span className="icon top-50 translate-middle-y">
                  <Icon icon="solar:lock-password-outline" />
                </span>
                <input
                  type="password"
                  value={isPassword}
                  onChange={(e) => setisPassword(e.target.value)}
                  className="form-control h-56-px bg-neutral-50 radius-12"
                  placeholder="Password"
                />
              </div>
              <span
                className="toggle-password ri-eye-line cursor-pointer position-absolute end-0 top-50 translate-middle-y me-16 text-secondary-light"
                data-toggle="#your-password"
              />
            </div>
            <div className="">
              <div className="d-flex justify-content-between gap-2">
                <div className="form-check style-check d-flex align-items-center">
                  <input
                    className="form-check-input border border-neutral-300"
                    type="checkbox"
                    defaultValue=""
                    id="remeber"
                  />
                  <label className="form-check-label" htmlFor="remeber">
                    Remember me{" "}
                  </label>
                </div>
                <Link to="#" className="text-primary-600 fw-medium">
                  Forgot Password?
                </Link>
              </div>
            </div>
            <button
              type="submit"
              className="btn btn-primary text-sm btn-sm px-12 py-16 w-100 radius-12 mt-32"
            >
              Sign In
            </button>
          </form>
        </div>
      </section>
    </div>
  );
};

export default SignInLayer;
