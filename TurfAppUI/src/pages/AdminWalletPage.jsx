import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";
import "../styles/AdminWalletPage.css";

const AdminWalletPage = () => {
  const { auth } = useAuth();
  const [wallet, setWallet] = useState(null);
  const [loading, setLoading] = useState(true);
  const ownerName = auth?.name;

  useEffect(() => {
    fetchWallet();
  }, []);

  const fetchWallet = async () => {
    try {
      const res = await axios.get(
        "http://localhost:8086/api/wallet/admin-wallet",
        {
          auth: {
            username: auth.email,
            password: auth.password,
          },
        },
      );
      setWallet(res.data);
      console.log(res.data);
    } catch (err) {
      alert("Failed to load wallet");
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <p className="loading">Loading wallet...</p>;

  return (
    <div className="wallet-container">
      <div className="wallet-card">
        <h2>Admin Wallet</h2>

        <div className="wallet-balance">
          <span>Current Balance</span>
          <h1>₹ {wallet}</h1>
        </div>

        <div className="wallet-info">
          <div>
            <span>Owner</span>
            <p>{ownerName}</p>
          </div>

          <div>
            <span>Role</span>
            <p>ADMIN</p>
          </div>
        </div>

        <p className="wallet-note">
          <b>Note</b> This amount includes all completed & active bookings.
        </p>
      </div>
    </div>
  );
};

export default AdminWalletPage;
