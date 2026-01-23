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
      <div className="nav-left">
        <h4 className="logo" onClick={() => navigate("/")}>
          App Name
        </h4>
      </div>

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

        {/* CLIENT */}
        {role === "CLIENT" && (
          <>
            <NavLink to="/client/dashboard" className="nav-link">
              Dashboard
            </NavLink>
            <NavLink to="/client/wallet/topup" className="nav-link">
              Wallet
            </NavLink>
          </>
        )}

        {/* ADMIN (if you add later) */}
        {role === "ADMIN" && (
          <>
            <NavLink to="/admin/dashboard" className="nav-link">
              Dashboard
            </NavLink>
            <NavLink to="/admin/users" className="nav-link">
              Users
            </NavLink>
          </>
        )}

        {/* LOGOUT (any logged-in user) */}
        {role && (
          <button className="logout-btn" onClick={handleLogout}>
            Logout
          </button>
        )}
      </div>
    </nav>
  );
};

export default AppNavbar;
