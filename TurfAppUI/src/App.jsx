import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./App.css";

import AppNavbar from "./components/AppNavbar";
import ProtectedRoute from "./components/ProtectedRoute";

import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";

import ClientDashboard from "./pages/ClientDashboard";
import TopUpPage from "./pages/TopUpPage";
import TurfBookingPage from "./pages/TurfBookingPage";

function App() {
  return (
    <Router>
      <AppNavbar />

      <div className="page-container">
        <Routes>
          {/* PUBLIC */}
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          {/* CLIENT */}
          <Route
            path="/client/dashboard"
            element={
              <ProtectedRoute allowedRole="CLIENT">
                <ClientDashboard />
              </ProtectedRoute>
            }
          />

          <Route
            path="/client/wallet/topup"
            element={
              <ProtectedRoute allowedRole="CLIENT">
                <TopUpPage />
              </ProtectedRoute>
            }
          />

          {/*TURF BOOKING (CLIENT ONLY) */}
          <Route
            path="/client/turf/book"
            element={
              <ProtectedRoute allowedRole="CLIENT">
                <TurfBookingPage />
              </ProtectedRoute>
            }
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
