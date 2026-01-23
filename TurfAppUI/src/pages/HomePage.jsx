import { Link } from "react-router-dom";
import "./HomePage.css";

const HomePage = () => {
  return (
    <div className="home-container">
      {/* Header */}
      <header className="home-header">
        <h1>Turf Booking System</h1>
        <nav>
          <Link to="/login" className="nav-link">Login</Link>
          <Link to="/register" className="nav-link">Register</Link>
          <Link to="/bookings" className="nav-link">My Bookings</Link>
        </nav>
      </header>

      {/* Hero Section */}
      <section className="hero">
        <h2>Book Your Turf Anytime, Anywhere</h2>
        <p>Find available slots, book instantly, and enjoy your game hassle-free.</p>
        <Link to="/book" className="btn-primary">Book Now</Link>
      </section>

      {/* Turf Highlights */}
      <section className="turf-section">
        <h3>Available Turfs</h3>
        <div className="turf-list">
          <div className="turf-card">
            <h4>Greenfield Turf</h4>
            <p>Location: Pune</p>
            <p>Price: ₹500/hour</p>
            <Link to="/book" className="btn-secondary">Book</Link>
          </div>
          <div className="turf-card">
            <h4>City Sports Arena</h4>
            <p>Location: Mumbai</p>
            <p>Price: ₹700/hour</p>
            <Link to="/book" className="btn-secondary">Book</Link>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="home-footer">
        <p>© 2026 Turf Booking System | Play. Book. Enjoy.</p>
      </footer>
    </div>
  );
};

export default HomePage;
