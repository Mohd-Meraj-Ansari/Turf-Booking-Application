import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./App.css";

import AppNavbar from "./components/AppNavbar";
import ProtectedRoute from "./components/ProtectedRoute";

import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";

//client pages
import ClientDashboard from "./pages/ClientDashboard";
import TopUpPage from "./pages/TopUpPage";
import TurfBookingPage from "./pages/TurfBookingPage";
import BookingHistoryPage from "./pages/BookingHistoryPage";
import UpdateProfile from "./pages/UpdateClientProfilePage";
// admin pages
import AdminDashboard from "./pages/AdminDashboard";
import AddTurfPage from "./pages/AddTurfPage";
import AddAccessoryPage from "./pages/AddAccessoryPage";
import TurfAvailabilityPage from "./pages/TurfAvailabilityPage";
import AllBookingsPage from "./pages/AllBookingPage";

function App() {
  return (
    <Router>
      <AppNavbar />

      <div className="page-container">
        <Routes>
          {/* public routes */}
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          {/* client only routes */}
          <Route
            path="/client/dashboard"
            element={
              <ProtectedRoute allowedRole="CLIENT">
                <ClientDashboard />
              </ProtectedRoute>
            }
          />
          {/* wallet */}
          <Route
            path="/client/wallet/topup"
            element={
              <ProtectedRoute allowedRole="CLIENT">
                <TopUpPage />
              </ProtectedRoute>
            }
          />

          {/*turf booking*/}
          <Route
            path="/client/turf/book"
            element={
              <ProtectedRoute allowedRole="CLIENT">
                <TurfBookingPage />
              </ProtectedRoute>
            }
          />

          {/* booking history */}
          <Route
            path="/client/bookings/history"
            element={
              <ProtectedRoute allowedRole="CLIENT">
                <BookingHistoryPage />
              </ProtectedRoute>
            }
          />
          {/* update details */}
          <Route
            path="/client/updateprofile"
            element={
              <ProtectedRoute allowedRole="CLIENT">
                <UpdateProfile />
              </ProtectedRoute>
            }
          />
          {/* admin routes */}

          {/* admin dashboard*/}
          <Route
            path="/admin/dashboard"
            element={
              <ProtectedRoute allowedRole="ADMIN">
                <AdminDashboard />
              </ProtectedRoute>
            }
          />

          {/* add turf*/}
          <Route
            path="/admin/addturf"
            element={
              <ProtectedRoute allowedRole="ADMIN">
                <AddTurfPage />
              </ProtectedRoute>
            }
          />

          {/* add accessory */}
          <Route
            path="/admin/addaccessory"
            element={
              <ProtectedRoute allowedRole="ADMIN">
                <AddAccessoryPage />
              </ProtectedRoute>
            }
          />

          {/* {add turf availability} */}
          <Route
            path="/admin/addavailability"
            element={
              <ProtectedRoute allowedRole="ADMIN">
                <TurfAvailabilityPage />
              </ProtectedRoute>
            }
          />

          {/* {all turf bookings} */}
          <Route
            path="/admin/allbookings"
            element={
              <ProtectedRoute allowedRole="ADMIN">
                <AllBookingsPage />
              </ProtectedRoute>
            }
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
