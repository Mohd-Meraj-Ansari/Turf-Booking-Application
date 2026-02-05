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
        <span className="logo-badge">BookMyTurf</span>
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

            <NavLink to="/client/turfstatus" className="nav-link">
              Turf Status
            </NavLink>

            <NavLink to="/client/bills" className="nav-link">
              Invoice
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
            <NavLink to="/admin/wallet" className="nav-link">
              Revenue
            </NavLink>
            <NavLink to="/admin/bookings" className="nav-link">
              Bookings
            </NavLink>
            <NavLink to="/admin/alltransactions" className="nav-link">
              All Transactions
            </NavLink>
            <NavLink to="/admin/turfstatus" className="nav-link">
              Turf Status
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
