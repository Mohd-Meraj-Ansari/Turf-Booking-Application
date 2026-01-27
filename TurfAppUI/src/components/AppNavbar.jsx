import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const AppNavbar = () => {
  const navigate = useNavigate();
  const { auth, logout } = useAuth();

  // Fallback for refresh (UI only)
  const role = auth?.role || localStorage.getItem("role");

  const handleLogout = () => {
    logout(); // clears AuthContext
    localStorage.clear(); // clears UI state
    navigate("/login");
  };

  return (
    <nav className="app-navbar">
      {/* LEFT */}
      <div className="logo">
        <span className="logo-badge" onClick={() => navigate("/")}>
          BookMyTurf
        </span>
        {/* <h4 className="logo" onClick={() => navigate("/")}>
          BookMyTurf
        </h4> */}
      </div>
      <div className="nav-left"></div>

      {/* RIGHT */}
      <div className="nav-right">
        {/* NOT LOGGED IN */}
        {!role && (
          <>
            <NavLink to="/" className="nav-link">
              Home
            </NavLink>
            <NavLink to="/login" className="nav-link">
              Login
            </NavLink>
            <NavLink to="/register" className="nav-link">
              Register
            </NavLink>
          </>
        )}

        {/* client nav items */}
        {role === "CLIENT" && (
          <>
            <NavLink to="/client/dashboard" className="nav-link">
              Dashboard
            </NavLink>
            <NavLink to="/client/wallet/topup" className="nav-link">
              Wallet
            </NavLink>
            <NavLink to="/client/turf/book" className="nav-link">
              Book Turf
            </NavLink>
            <NavLink to="/client/bookings/history" className="nav-link">
              Booking History
            </NavLink>

            <NavLink to="/client/updateprofile" className="nav-link">
              Update Profile
            </NavLink>
          </>
        )}

        {/* admin nav items */}
        {role === "ADMIN" && (
          <>
            <NavLink to="/admin/dashboard" className="nav-link">
              Dashboard
            </NavLink>
            <NavLink to="/admin/addturf" className="nav-link">
              Add Turf
            </NavLink>
            <NavLink to="/admin/addaccessory" className="nav-link">
              Add Accessory
            </NavLink>
            <NavLink to="/admin/addavailability" className="nav-link">
              Add Turf Schedule
            </NavLink>
            <NavLink to="/admin/allbookings" className="nav-link">
              All Bookings
            </NavLink>
          </>
        )}

        {/* LOGOUT (any logged-in user) */}
        {role && (
          <button className="btn btn-danger" onClick={handleLogout}>
            Logout
          </button>
        )}
      </div>
    </nav>
  );
};

export default AppNavbar;
